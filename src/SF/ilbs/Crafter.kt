package SF.ilbs

import arc.Events
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.math.geom.Geometry
import arc.scene.ui.layout.Table
import arc.struct.EnumSet
import arc.struct.Seq
import arc.util.Eachable
import arc.util.Nullable
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.content.Fx
import mindustry.content.Liquids
import mindustry.entities.Effect
import mindustry.entities.units.BuildPlan
import mindustry.game.EventType
import mindustry.gen.Building
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.logic.LAccess
import mindustry.type.Item
import mindustry.type.ItemStack
import mindustry.type.Liquid
import mindustry.type.LiquidStack
import mindustry.ui.Bar
import mindustry.world.Block
import mindustry.world.draw.DrawBlock
import mindustry.world.draw.DrawDefault
import mindustry.world.meta.BlockFlag
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import mindustry.world.meta.StatValues
import java.util.*


/**
 * 扩展接口声明：
 * 原作@Anuken
 * 扩展@ZenXS
 * 示例项目：content/block
 * Json自己摸索，本人因为不会json所以来写Kotlin
 * 功能:
 * 1.完全支持原版@GenericCrafter的所有功能
 * 2.扩展功能如下（所有功能皆为可选）：
 * (1) 动态craftTime
 * 计算方式：
 * craftTime = craftTime - bonusItem数量/2 * Bonus
 * bonusItem: 加速物品
 * Bonus: 加速倍率
 * 当craftTime <= 0 时， 设置 craftTime = 10
 * (2)类似钍反的的热量系统
 * 热量增加方式：
 * heat += 0.01 * heatItem数量 / heatd
 * heatItem: 反应物品
 * heatd: 热容量
 * heatLiquid: 散热液体
 * 爆炸特效需自己写
 */
open class BonusCrafter(name: String?) : Block(name) {
    /** Written to outputItems as a single-element array if outputItems is null.  */
    @Nullable
    var outputItem: ItemStack? = null

    /** Overwrites outputItem if not null.  */
    @Nullable
    var outputItems: Array<ItemStack>? = null

    /** Written to outputLiquids as a single-element array if outputLiquids is null.  */
    @Nullable
    var outputLiquid: LiquidStack? = null

    /** Overwrites outputLiquid if not null.  */
    @Nullable
    var outputLiquids: Array<LiquidStack>? = null

    /** Liquid output directions, specified in the same order as outputLiquids. Use -1 to dump in every direction. Rotations are relative to block.  */
    var liquidOutputDirections = intArrayOf(-1)

    /** if true, crafters with multiple liquid outputs will dump excess when there's still space for at least one liquid type  */
    var dumpExtraLiquid = true
    var ignoreLiquidFullness = false
    var craftTime = 80f
    var craftEffect: Effect = Fx.none
    var updateEffect: Effect = Fx.none
    var updateEffectChance = 0.04f
    var warmupSpeed = 0.019f
    var spBonus: Int = 0
    var bonus: Int = 1
    var bonusItem: Item? = null
    var heatItem: Item? = null
    var heatd = 5f
    var heatLiquids: Liquid? = null
    /** Only used for legacy cultivator blocks.  */
    var legacyReadWarmup = false
    var drawer: DrawBlock = DrawDefault()
    init {
        update = true
        rotate = false
        solid = true
        hasItems = true
        ambientSound = Sounds.machine
        sync = true
        ambientSoundVolume = 0.03f
        flags = EnumSet.of(BlockFlag.factory)
        drawArrow = false
    }
    override fun setStats() {
        stats.timePeriod = BonusCrafterBuild().carft
        super.setStats()
        if (hasItems && itemCapacity > 0 || outputItems != null) {
            stats.add(Stat.productionTime, BonusCrafterBuild().carft / 60f, StatUnit.seconds)
        }
        if (outputItems != null) {
            stats.add(Stat.output, StatValues.items(BonusCrafterBuild().carft, *outputItems!!))
        }
        if (outputLiquids != null) {
            stats.add(Stat.output, StatValues.liquids(1f, *outputLiquids!!))
        }
    }
    override fun setBars() {
        super.setBars()
        addBar("效率") { bounds: Building -> Bar("效率: " + (bounds.items.get(bonusItem).toFloat() / bounds.block.itemCapacity.toFloat()) * 100 + "%" , Pal.lightOrange) { (bounds.items.get(bonusItem).toFloat() / bounds.block.itemCapacity.toFloat()) } }
        if (heatItem != null) {
            addBar("热量") { bounds: BonusCrafterBuild ->
                Bar(
                    "热量", Color.red
                ) { bounds.heat }
            }
        }
        //set up liquid bars for liquid outputs
        if (outputLiquids != null && outputLiquids!!.size > 0) {
            //no need for dynamic liquid bar
            removeBar("liquid")

            //then display output buffer
            for (stack in outputLiquids!!) {
                addLiquidBar(stack.liquid)
            }
        }
    }

    override fun rotatedOutput(x: Int, y: Int): Boolean {
        return false
    }
    override fun load() {
        super.load()
        drawer.load(this)
    }

    override fun init() {
        if (outputItems == null && outputItem != null) {
            outputItems = arrayOf(outputItem!!)
        }
        if (outputLiquids == null && outputLiquid != null) {
            outputLiquids = arrayOf(outputLiquid!!)
        }
        //write back to outputLiquid, as it helps with sensing
        if (outputLiquid == null && outputLiquids != null && outputLiquids!!.size > 0) {
            outputLiquid = outputLiquids!![0]
        }
        outputsLiquid = outputLiquids != null
        if (outputItems != null) hasItems = true
        if (outputLiquids != null) hasLiquids = true
        super.init()
    }

    override fun drawPlanRegion(plan: BuildPlan, list: Eachable<BuildPlan>) {
        drawer.drawPlan(this, plan, list)
    }

    public override fun icons(): Array<TextureRegion> {
        return drawer.finalIcons(this)
    }

    override fun outputsItems(): Boolean {
        return outputItems != null
    }

    override fun getRegionsToOutline(out: Seq<TextureRegion>) {
        drawer.getRegionsToOutline(this, out)
    }

    override fun drawOverlay(x: Float, y: Float, rotation: Int) {
        if (outputLiquids != null) {
            for (i in outputLiquids!!.indices) {
                val dir = if (liquidOutputDirections.size > i) liquidOutputDirections[i] else -1
                if (dir != -1) {
                    Draw.rect(
                        outputLiquids!![i].liquid.fullIcon,
                        x + Geometry.d4x(dir + rotation) * (size * Vars.tilesize / 2f + 4),
                        y + Geometry.d4y(dir + rotation) * (size * Vars.tilesize / 2f + 4),
                        8f, 8f
                    )
                }
            }
        }
    }
    open inner class BonusCrafterBuild : Building() {
        var heat = 0f
        var carft = craftTime
        var taboble: Table? = null
        var progress = 0f
        var totalProgress = 0f
        var warmup = 0f
        var message = StringBuilder()
        override fun buildConfiguration(table: Table?) {
        }
        override fun draw() {
            drawer.draw(this)
        }

        override fun drawLight() {
            super.drawLight()
            drawer.drawLight(this)
        }

        override fun shouldConsume(): Boolean {
            if (outputItems != null) {
                for (output in outputItems!!) {
                    if (items[output.item] + output.amount > itemCapacity) {
                        return false
                    }
                }
            }
            if (outputLiquids != null && !ignoreLiquidFullness) {
                var allFull = true
                for (output in outputLiquids!!) {
                    if (liquids[output.liquid] >= liquidCapacity - 0.001f) {
                        if (!dumpExtraLiquid) {
                            return false
                        }
                    } else {
                        //if there's still space left, it's not full for all liquids
                        allFull = false
                    }
                }

                //if there is no space left for any liquid, it can't reproduce
                if (allFull) {
                    return false
                }
            }
            return enabled
        }
        override fun update() {
            super.update()
            if (bonusItem != null) {
                spBonus = (items.get(heatItem).toFloat() / 2 * bonus).toInt()
                if (craftTime > spBonus) {
                    carft = craftTime - spBonus
                } else {
                    carft = 10f
                }
            }
            //heat
            if (heatItem != null) {
                var it = items.get(heatItem)
                var liq = liquids.get(heatLiquids)
                    if (liq <= 1f) {
                            if (it != 0) {
                                heat += 0.01f * it.toFloat() / heatd
                            }
                    } else {
                        if (heat != 0f) {
                            heat -= 0.01f
                        }
                    }
                if (heat >= 0.999f) {
                    Events.fire(EventType.Trigger.thoriumReactorOverheat)
                    kill()
                }
                }
            }
        override fun updateTile() {
            if (efficiency > 0) {
                progress += getProgressIncrease(carft)
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed)

                //continuously output based on efficiency
                if (outputLiquids != null) {
                    val inc = getProgressIncrease(1f)
                    for (output in outputLiquids!!) {
                        handleLiquid(
                            this,
                            output.liquid,
                            Math.min(output.amount * inc, liquidCapacity - liquids[output.liquid])
                        )
                    }
                }
                if (wasVisible && Mathf.chanceDelta(updateEffectChance.toDouble())) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4))
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed)
            }

            //TODO may look bad, revert to edelta() if so
            totalProgress += warmup * Time.delta
            if (progress >= 1f) {
                craft()
            }
            dumpOutputs()
        }

        override fun getProgressIncrease(baseTime: Float): Float {
            if (ignoreLiquidFullness) {
                return super.getProgressIncrease(baseTime)
            }

            //limit progress increase by maximum amount of liquid it can produce
            var scaling = 1f
            var max = 1f
            if (outputLiquids != null) {
                max = 0f
                for (s in outputLiquids!!) {
                    val value = (liquidCapacity - liquids[s.liquid]) / (s.amount * edelta())
                    scaling = Math.min(scaling, value)
                    max = Math.max(max, value)
                }
            }

            //when dumping excess take the maximum value instead of the minimum.
            return super.getProgressIncrease(baseTime) * if (dumpExtraLiquid) Math.min(max, 1f) else scaling
        }

        open fun warmupTarget(): Float {
            return 1f
        }

        override fun warmup(): Float {
            return warmup
        }

        override fun totalProgress(): Float {
            return totalProgress
        }

        fun craft() {
            consume()
            if (outputItems != null) {
                for (output in outputItems!!) {
                    for (i in 0 until output.amount) {
                        offload(output.item)
                    }
                }
            }
            if (wasVisible) {
                craftEffect.at(x, y)
            }
            progress %= 1f
        }

        fun dumpOutputs() {
            if (outputItems != null && timer(timerDump, dumpTime / timeScale)) {
                for (output in outputItems!!) {
                    dump(output.item)
                }
            }
            if (outputLiquids != null) {
                for (i in outputLiquids!!.indices) {
                    val dir = if (liquidOutputDirections.size > i) liquidOutputDirections[i] else -1
                    dumpLiquid(outputLiquids!![i].liquid, 2f, dir)
                }
            }
        }

        override fun sense(sensor: LAccess): Double {
            if (sensor == LAccess.progress) return progress().toDouble()
            //attempt to prevent wild total liquid fluctuation, at least for crafters
            return if (sensor == LAccess.totalLiquids && outputLiquid != null) liquids[outputLiquid!!.liquid].toDouble() else super.sense(
                sensor
            )
        }

        override fun progress(): Float {
            return Mathf.clamp(progress)
        }

        override fun getMaximumAccepted(item: Item): Int {
            return itemCapacity
        }

        override fun shouldAmbientSound(): Boolean {
            return efficiency > 0
        }

        override fun write(write: Writes) {
            super.write(write)
            write.f(progress)
            write.f(warmup)
            if (legacyReadWarmup) write.f(0f)
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            progress = read.f()
            warmup = read.f()
            if (legacyReadWarmup) read.f()
        }
    }

}


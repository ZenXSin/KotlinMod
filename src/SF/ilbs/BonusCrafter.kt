package SF.ilbs

import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.math.geom.Geometry
import arc.struct.EnumSet
import arc.struct.Seq
import arc.util.Eachable
import arc.util.Nullable
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.content.Fx
import mindustry.entities.units.BuildPlan
import mindustry.gen.Building
import mindustry.gen.Sounds
import mindustry.logic.LAccess
import mindustry.type.Item
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.world.Block
import mindustry.world.draw.DrawBlock
import mindustry.world.draw.DrawDefault
import mindustry.world.meta.BlockFlag
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import mindustry.world.meta.StatValues

/**
 *useritem: 加成物品
 * bonus: 加成倍率(默认1)
 *加成倍率计算：useritem/10 * bonus
 *by - zxs
 * (继承@Block
 * 修改GenericCrafter)
* */
abstract class BonusCrafter(name: String?) : Block(name) {
    /** Written to outputItems as a single-element array if outputItems is null.  */
    @Nullable
    var outputItem: ItemStack? = null

    /** Overwrites outputItem if not null.  */
    abstract var outputItems: Array<ItemStack>?

    /** Written to outputLiquids as a single-element array if outputLiquids is null.  */
    @Nullable
    var outputLiquid: LiquidStack? = null

    /** Overwrites outputLiquid if not null.  */
    abstract var outputLiquids: Array<LiquidStack>?

    /** Liquid output directions, specified in the same order as outputLiquids. Use -1 to dump in every direction. Rotations are relative to block.  */
    var liquidOutputDirections = intArrayOf(-1)

    /** if true, crafters with multiple liquid outputs will dump excess when there's still space for at least one liquid type  */
    var dumpExtraLiquid = true
    var ignoreLiquidFullness = false
    var craftTime = 80f
    var useitem: Item? = null
    var bonus: Int = 1
    var bons = 0
    var craftEffect = Fx.none
    var updateEffect = Fx.none
    var updateEffectChance = 0.04f
    var warmupSpeed = 0.019f

    /** Only used for legacy cultivator blocks.  */
    var legacyReadWarmup = false
    var drawer: DrawBlock = DrawDefault()

    init {
        update = true
        solid = true
        hasItems = true
        ambientSound = Sounds.machine
        sync = true
        ambientSoundVolume = 0.03f
        flags = EnumSet.of(BlockFlag.factory)
        drawArrow = false
    }

    override fun setStats() {
        stats.timePeriod = craftTime - bons
        super.setStats()
        if (hasItems && itemCapacity > 0 || outputItems != null) {
            stats.add(Stat.productionTime, craftTime - bons / 60f, StatUnit.seconds)
        }
        if (outputItems != null) {
            stats.add(Stat.output, StatValues.items(craftTime - bons, *outputItems!!))
        }
        if (outputLiquids != null) {
            stats.add(Stat.output, StatValues.liquids(1f, *outputLiquids!!))
        }
    }

    override fun setBars() {
        super.setBars()

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

    open inner class GenericCrafterBuild : Building() {
        var progress = 0f
        var totalProgress = 0f
        var warmup = 0f
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

        override fun updateTile() {
            bons = items.get(useitem)/10 * bonus
            if (efficiency > 0) {
                progress += getProgressIncrease(craftTime - bons)
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

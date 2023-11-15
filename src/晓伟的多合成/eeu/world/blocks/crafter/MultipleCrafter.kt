package 晓伟的多合成.eeu.world.blocks.crafter


import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.math.geom.Geometry
import arc.scene.ui.Button
import arc.scene.ui.ButtonGroup
import arc.scene.ui.layout.Table
import arc.struct.EnumSet
import arc.struct.Seq
import arc.util.Eachable
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import eeu.other.Formula
import 晓伟的多合成.eeu.other.stats.StatValues
import 晓伟的多合成.eeu.other.stats.Stats
import mindustry.Vars
import mindustry.entities.units.BuildPlan
import mindustry.gen.Building
import mindustry.gen.Sounds
import mindustry.logic.LAccess
import mindustry.type.Item
import mindustry.type.Liquid
import mindustry.ui.Styles
import mindustry.world.Block
import mindustry.world.consumers.ConsumeLiquid
import mindustry.world.consumers.ConsumeLiquids
import mindustry.world.draw.DrawBlock
import mindustry.world.draw.DrawDefault
import mindustry.world.meta.BlockFlag
import 晓伟的多合成.eeu.other.FormulaStack


class MultipleCrafter(name: String?) : Block(name) {
    var formulas: FormulaStack
    var dumpExtraLiquid = true
    var ignoreLiquidFullness = false
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
        formulas = FormulaStack()
        configurable = true
        config(
            Int::class.java
        ) { build: Building, value: Int ->
            (build as MultipleCrafterBuilding).setIndex(
                value
            )
        }
    }

    override fun setStats() {
        super.setStats()
        stats.add(Stats.formula, StatValues.formulas(formulas, this))
    }

    override fun setBars() {
        super.setBars()
        var added = false
        val addedLiquids = Seq<Liquid>()
        for (f in formulas.formulas) {
            if (f.input != null) for (cons in f.getInputs()) {
                if (cons is ConsumeLiquid) {
                    added = true
                    if (addedLiquids.contains(cons.liquid)) continue
                    addedLiquids.add(cons.liquid)
                    addLiquidBar(cons.liquid)
                } else if (cons is ConsumeLiquids) {
                    added = true
                    for (stack in cons.liquids) {
                        if (addedLiquids.contains(stack.liquid)) continue
                        addedLiquids.add(stack.liquid)
                        addLiquidBar(stack.liquid)
                    }
                }
            }
            if (f.getOutputLiquids() != null) for (out in f.getOutputLiquids()) {
                if (addedLiquids.contains(out.liquid)) continue
                addedLiquids.add(out.liquid)
                addLiquidBar<Building>(out.liquid)
            }
            if (!added) {
                addLiquidBar { build: Building -> build.liquids.current() }
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
        super.init()
        formulas.apply(this)
    }

    override fun drawPlanRegion(plan: BuildPlan, list: Eachable<BuildPlan>) {
        drawer.drawPlan(this, plan, list)
    }

    public override fun icons(): Array<TextureRegion> {
        return drawer.finalIcons(this)
    }

    override fun outputsItems(): Boolean {
        return formulas.outputItems()
    }

    override fun getRegionsToOutline(out: Seq<TextureRegion>) {
        drawer.getRegionsToOutline(this, out)
    }

    inner class MultipleCrafterBuilding : Building() {
        var progress = 0f
        var totalProgress = 0f
        var warmup = 0f
        var formulaIndex = 0
        var formula: Formula = formulas.getFormula(formulaIndex)
        var outputItems = formula.outputItems
        var outputLiquids = formula.outputLiquids
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

        override fun updateConsumption() {
            //everything is valid when cheating
            if (formula.inputs == null || cheating()) {
                potentialEfficiency = if (enabled && productionValid()) 1f else 0f
                optionalEfficiency = if (shouldConsume()) potentialEfficiency else 0f
                efficiency = optionalEfficiency
                updateEfficiencyMultiplier()
                return
            }

            //disabled -> nothing works
            if (!enabled) {
                optionalEfficiency = 0f
                efficiency = optionalEfficiency
                potentialEfficiency = efficiency
                return
            }
            val update = shouldConsume() && productionValid()
            var minEfficiency = 1f

            //assume efficiency is 1 for the calculations below
            optionalEfficiency = 1f
            efficiency = optionalEfficiency

            //first pass: get the minimum efficiency of any consumer
            for (cons in formula.inputs!!) {
                minEfficiency = Math.min(minEfficiency, cons.efficiency(self()))
            }

            //same for optionals
            for (cons in formula.inputs!!) {
                optionalEfficiency = Math.min(optionalEfficiency, cons.efficiency(self()))
            }

            //efficiency is now this minimum value
            efficiency = minEfficiency
            optionalEfficiency = efficiency

            //assign "potential"
            potentialEfficiency = efficiency

            //no updating means zero efficiency
            if (!update) {
                optionalEfficiency = 0f
                efficiency = optionalEfficiency
            }
            updateEfficiencyMultiplier()

            //second pass: update every consumer based on efficiency
            if (update && efficiency > 0) {
                formula.update(this)
            }
        }

        override fun displayConsumption(table: Table) {
            super.displayConsumption(table)
            formula.build(this, table)
        }

        override fun updateTile() {
            formula = formulas.getFormula(formulaIndex)
            outputItems = formula.outputItems
            outputLiquids = formula.outputLiquids
            if (efficiency > 0) {
                progress += getProgressIncrease(formula.craftTime)
                warmup = Mathf.approachDelta(warmup, warmupTarget(), formula.warmupSpeed)

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
                if (wasVisible && Mathf.chanceDelta(formula.updateEffectChance.toDouble())) {
                    formula.updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4))
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, formula.warmupSpeed)
            }
            totalProgress += warmup * Time.delta
            if (progress >= 1f) {
                craft()
            }
            dumpOutputs()
        }

        override fun drawSelect() {
            super.drawSelect()
            if (outputLiquids != null) {
                for (i in outputLiquids!!.indices) {
                    val dir = if (formula.liquidOutputDirections.size > i) formula.liquidOutputDirections[i] else -1
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

        fun warmupTarget(): Float {
            return 1f
        }

        override fun warmup(): Float {
            return warmup
        }

        override fun totalProgress(): Float {
            return totalProgress
        }

        fun craft() {
            formulas.trigger(this)
            if (outputItems != null) {
                for (output in outputItems!!) {
                    for (i in 0 until output.amount) {
                        offload(output.item)
                    }
                }
            }
            if (wasVisible) {
                formula.craftEffect.at(x, y)
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
                    val dir = if (formula.liquidOutputDirections.size > i) formula.liquidOutputDirections[i] else -1
                    dumpLiquid(outputLiquids!![i].liquid, 2f, dir)
                }
            }
        }

        override fun buildConfiguration(table: Table) {
            super.buildConfiguration(table)
            table.pane { t: Table ->
                val group =
                    ButtonGroup<Button>()
                for (i in 0 until formulas.size()) {
                    val b =
                        t.button(i.toString() + "", Styles.squareTogglet) {
                            group.setChecked(i.toString() + "")
                            configure(i)
                        }.size(45f).get()
                    group.add(b)
                }
                group.setChecked(formulaIndex.toString() + "")
            }
        }

        override fun sense(sensor: LAccess): Double {
            return if (sensor == LAccess.progress) progress().toDouble() else super.sense(sensor)
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

        fun setIndex(index: Int) {
            formulaIndex = index
        }

        override fun write(write: Writes) {
            super.write(write)
            write.f(progress)
            write.f(warmup)
            write.b(formulaIndex)
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            progress = read.f()
            warmup = read.f()
            formulaIndex = read.b().toInt()
        }
    }
}
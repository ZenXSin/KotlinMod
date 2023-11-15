package eeu.other

import arc.scene.ui.layout.Table
import arc.util.Nullable
import mindustry.content.Fx
import mindustry.gen.Building
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.world.Block
import mindustry.world.consumers.Consume
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import mindustry.world.meta.StatValues
import mindustry.world.meta.Stats
import java.util.*

class Formula {
    @Nullable
    var inputs: Array<Consume>? = null

    @Nullable
    var outputItems: Array<ItemStack>? = null

    @Nullable
    var outputLiquids: Array<LiquidStack>? = null
    var craftTime = 60f
    var liquidOutputDirections = intArrayOf(-1)
    var craftEffect = Fx.none
    var updateEffect = Fx.none
    var updateEffectChance = 0.04f
    var warmupSpeed = 0.019f
    fun setInput(input: Array<Consume>?) {
        inputs = input
    }

    fun setOutput(outputItems: Array<ItemStack>?) {
        this.outputItems = outputItems
    }

    fun setOutput(outputLiquids: Array<LiquidStack>?) {
        this.outputLiquids = outputLiquids
    }

    operator fun set(`in`: Array<Consume>?, outputItems: Array<ItemStack>?, outputLiquids: Array<LiquidStack>?) {
        inputs = `in`
        this.outputItems = outputItems
        this.outputLiquids = outputLiquids
    }

    fun apply(block: Block?) {
        if (inputs == null) return
        for (c in inputs!!) {
            c.apply(block)
        }
    }

    fun update(build: Building?) {
        if (inputs == null) return
        for (c in inputs!!) {
            c.update(build)
        }
    }

    fun trigger(build: Building?) {
        if (inputs == null) return
        for (c in inputs!!) {
            c.trigger(build)
        }
    }

    fun display(stats: Stats, block: Block) {
        stats.timePeriod = craftTime
        if (inputs != null) for (c in inputs!!) {
            c.display(stats)
        }
        if (block.hasItems && block.itemCapacity > 0 || outputItems != null) {
            stats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds)
        }
        if (outputItems != null) {
            stats.add(Stat.output, StatValues.items(craftTime, *outputItems!!))
        }
        if (outputLiquids != null) {
            stats.add(Stat.output, StatValues.liquids(1f, *outputLiquids!!))
        }
    }

    fun build(build: Building?, table: Table) {
        if (inputs == null) return
        table.pane { t: Table? ->
            for (c in inputs!!) {
                c.build(build, t)
            }
        }
    }

    override fun toString(): String {
        return "Formula{" +
                "input=" + Arrays.toString(inputs) +
                ", outputItems=" + Arrays.toString(outputItems) +
                ", outputLiquids=" + Arrays.toString(outputLiquids) +
                ", craftTime=" + craftTime +
                '}'
    }
}
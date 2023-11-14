package SF.content

import SF.ilbs.*
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.type.ItemStack
import zinxs.Draw

object test {
    var 归 : BonusCrafter? = null
    @JvmStatic
    fun load() {
        归 = object : BonusCrafter("归") {
            init {
                outputItem = ItemStack(Items.phaseFabric, 5)
                consumeItems(*ItemStack.with(Items.thorium,5,Items.silicon,10))
                consumeLiquid(Liquids.cryofluid, 5f)
                consumePower(5f)
                configurable = true
                bonus = 10
                bonusItem = Items.thorium
                heatItem = Items.thorium
                heatLiquids = Liquids.cryofluid
                heatd = 10f
                health = 1200
                size = 3
                hasPower = true
                itemCapacity = 40
                hasItems = true
                hasLiquids = true
                rotate = false
                solid = true
                liquidCapacity = 2f
                description = "利用钍在衰变时散发的辐射，快速制造布\n[red]需要使用冷却液来确保不会发生爆炸"
                updateEffect = Fx.fuelburn
                craftEffect = Fx.pulverizeMedium
                drawer = Draw.build(2.6f, 3.5342917f, -3.5342917f, 4, 0f, 5f)
            }
        }
    }
}
package SF.content

import SF.ilbs.*
import arc.scene.ui.layout.Table
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.content.TechTree
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.world.blocks.logic.SwitchBlock
import mindustry.world.blocks.production.GenericCrafter
import zinxs.Block
import zinxs.Draw
import zinxs.Item

object Block {
    var 归中 : BonusCrafter? = null
    @JvmStatic
    fun load() {
        归中 = object : BonusCrafter("归中") {
            init {
                craftTime = 240f
                health = 1200
                size = 3
                hasPower = true
                itemCapacity = 40
                hasItems = true
                hasLiquids = true
                rotate = false
                solid = true
                consumePower(11f)
                consumeItems(ItemStack(Items.thorium, 1), ItemStack(Items.sand, 10))
                consumeLiquid(Liquids.cryofluid, 0.5f).booster = true
                liquidCapacity = 2f
                description = "利用钍在衰变时散发的辐射，快速制造布\n[red]需要使用冷却液来确保不会发生爆炸"
                updateEffect = Fx.fuelburn
                craftEffect = Fx.pulverizeMedium
                useitem = Items.thorium
                bonus = 10
                }
        }
        }
    }
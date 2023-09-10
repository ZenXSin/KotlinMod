package zinxs

import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.draw.*
import kotlin.math.sin


object Block {
    var 临时协议签署所: GenericCrafter? = null
    var 衰变布厂: GenericCrafter? = null
    @JvmStatic
    fun load() {
        临时协议签署所 = object : GenericCrafter("临时协议签署所")  {
            init {
                requirements(Category.crafting, ItemStack.with(Items.copper, 250, Items.lead, 350, Items.graphite, 250, Items.surgeAlloy, 200))
                craftTime = 1200f
                //60f为一秒
                size = 2
                health = 400
                hasPower = true
                hasItems = true
                hasLiquids = false
                rotate = false
                solid = true
                consumePower(18.8f)
                outputItems = arrayOf(ItemStack(Item.临时协议, 1))
                description = "基础签署措施，可用签署于小型武器和工厂的建筑协议"
            }
        }
        衰变布厂 = object : GenericCrafter("衰变布厂")  {
            init {
                requirements(Category.crafting, ItemStack.with(Items.graphite, 150, Items.lead, 150, Items.graphite, 150, Items.surgeAlloy, 100, Items.titanium, 50, Items.thorium, 100, Item.临时协议, 2))
                craftTime = 80f
                health = 1200
                size = 3
                hasPower = true
                itemCapacity = 40
                hasItems = true
                hasLiquids = true
                rotate = false
                solid = true
                consumePower(11f)
                consumeItems(ItemStack(Items.thorium, 5), ItemStack(Items.sand, 10))
                consumeLiquid(Liquids.cryofluid, 3f).booster = true
                liquidCapacity = 10f
                outputItems = arrayOf(ItemStack(Items.phaseFabric, 5))
                description = "利用钍在衰变时散发的辐射，快速制造布\n[red]需要使用冷却液来确保不会发生爆炸"
                updateEffect = Fx.fuelburn
                craftEffect = Fx.pulverizeMedium
                drawer = Draw.build(2.6f, 3.5342917f, 0f, 4, 0f, 4f)
            }
        }
    }
}
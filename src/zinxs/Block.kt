package zinxs

import mindustry.content.Items
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.blocks.production.GenericCrafter


object Block {
    var 临时协议签署所: GenericCrafter? = null
    @JvmStatic
    fun load() {
        临时协议签署所 = object : GenericCrafter("临时协议签署所")  {
            init {
                requirements(Category.crafting, ItemStack.with(Items.copper, 250, Items.lead, 350, Items.graphite, 250, Items.surgeAlloy, 200))
                craftTime = 1200f
                size = 2
                hasPower = true
                hasItems = true
                hasLiquids = false
                rotate = false
                solid = true
                consumePower(25f)
                outputItems = arrayOf(ItemStack(Item.临时协议, 1))
            }
        }
    }
}
package example

import arc.graphics.Color
import mindustry.content.Blocks
import mindustry.content.Items
import mindustry.type.Category
import mindustry.type.Item
import mindustry.type.ItemStack
import mindustry.world.blocks.production.GenericCrafter

private var 临时协议: Item? = null
private var 临时协议签署所: GenericCrafter? = null
object Item {
    @JvmStatic
        fun load() {
        临时协议 = object : Item("临时协议", Color.red){
            init {
                alwaysUnlocked = false
            }
        }
    }
}
object Blocks {
    @JvmStatic
    fun load() {
        临时协议签署所 = object : GenericCrafter("临时协议签署所")  {
            init {
                size = 2
                hasPower = true
                hasItems = true
                update = true
                health = 2000
                description = "基础签署措施，可用签署于小型武器和工厂的建筑协议"
                outputItem = ItemStack(临时协议, 1)
                itemCapacity = 25
                craftTime = 4000f
                category = Category.effect
                consumePower(10f)
                requirements = ItemStack.with(Items.copper, 250, Items.lead, 350, Items.graphite, 250, Items.surgeAlloy, 200)
            }
        }
    }
}

object TT {
    @JvmStatic
    fun loadTechTree() {
        TechTree.addToNode(临时协议签署所!!, { TechTree.node(Blocks.duo) })

    }
}
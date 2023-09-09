package example

import arc.graphics.Color
import mindustry.content.Items
import mindustry.type.Category
import mindustry.type.Item
import mindustry.type.ItemStack
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.meta.Env

private var 临时协议: Item? = null
private var 自塑合金: Item? = null
object Item {
    @JvmStatic
        fun load() {
        临时协议 = object : Item("临时协议", Color.red){
            init {
                alwaysUnlocked = false
            }
        }
        自塑合金 = object : Item("自塑合金", Color.red){
            init {
                alwaysUnlocked = false
                radioactivity = 0f
                explosiveness = 0f
                flammability = 0f
            }
        }
    }
}

private var 星际实验室: GenericCrafter? = null
object Blocks {
    @JvmStatic
    fun load() {
        星际实验室 = object : GenericCrafter("星际实验室")  {
            init {
                size = 2
                hasPower
                hasItems
                health = 2000
                description = "基础协议，可用于小型武器和工厂的建筑"
                outputItem = ItemStack(临时协议, 1)
                itemCapacity = 25
                craftTime = 4000f
                category = Category.effect
                consumePower(10f)
                envEnabled = envEnabled or Env.space
                requirements(Category.crafting, ItemStack.with(Items.copper, 250, Items.lead, 350, Items.graphite, 250, Items.surgeAlloy, 200))
            }
        }
    }
}
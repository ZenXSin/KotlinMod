package zinxs

import arc.graphics.Color
import arc.scene.ui.layout.Table
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.content.TechTree
import mindustry.entities.Effect
import mindustry.gen.Call
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.blocks.logic.SwitchBlock.SwitchBuild
import mindustry.world.blocks.production.GenericCrafter


object Block {
    var 临时协议签署所: GenericCrafter? = null
    var 衰变布厂: GenericCrafter? = null
    @JvmStatic
    fun load() {
        临时协议签署所 = object : GenericCrafter("临时协议签署所") {
            init {/*
                requirements(
                    Category.crafting,
                    ItemStack.with(Items.copper, 250, Items.lead, 350, Items.graphite, 250, Items.surgeAlloy, 200)
                )*/
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
                outputItem = ItemStack(Item.临时协议, 1)
                description = "基础签署措施，可用签署于小型武器和工厂的建筑协议"
            }
        }
        衰变布厂 = object : GenericCrafter("衰变布厂") {
            val table = Table()
            init {
                configurable = true
                requirements(
                    Category.crafting,
                    ItemStack.with(
                        Items.graphite,
                        150,
                        Items.lead,
                        150,
                        Items.graphite,
                        150,
                        Items.surgeAlloy,
                        100,
                        Items.titanium,
                        50,
                        Items.thorium,
                        100,
                        Item.临时协议,
                        2
                    )
                )
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
                drawer = Draw.build(2.6f, 3.5342917f, -3.5342917f, 4, 0f, 5f)
                config(
                    Boolean::class.java
                ) { entity: SwitchBuild, b: Boolean? -> entity.enabled = b!! }
                /*table.update {
                    table.button("布") {
                        if (Vars.mobile) {
                            outputItem = ItemStack(Items.phaseFabric, 5)
                        } else {
                            outputItem = null
                        }
                    }
                }*/
                TechTree.nodeRoot("测试", 临时协议签署所, true)
                    {
                        TechTree.node(衰变布厂)
                    }
                }
            }
        }
    }

val map by lazy {
    Fx::class.java.fields.filter { it.type == Effect::class.java }
        .associate { it.name to (it.get(null) as Effect) }
}
fun effect(effect:String, bigger:Float, color: Color, x:Float, y:Float) {
    val type = effect.let { map[it] }!!
    Call.effect(type,x,y,bigger,color)
}
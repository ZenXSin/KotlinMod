package SF.ilbs

import mindustry.ui.dialogs.BaseDialog
import mindustry.type.ItemStack
import mindustry.world.blocks.production.GenericCrafter
import arc.util.Log
import mindustry.gen.Building
import mindustry.graphics.Pal
import mindustry.ui.Bar

/**
 * @author: zxs
 * @bonus: 加成倍率
 * @bonusitems: 加成物品组（物品(ItemStack),权重(Float) 百分制，需和为100%(1)）
 * 实际生产耗时 = 默认生产耗时 - 加成物品总和 / 加成物品组数量 / 建筑物品容量 / 2 * 加成倍率
**/
abstract class BonusCrafter(name: String?) : GenericCrafter(name) {
    var bonus = 1f
    abstract var bonusitems : Array<Pair<ItemStack,Float>>?/**物品(ItemStack),权重(Float) 百分制，需和为100%(1)**/
    fun getBonus(build:Building):Float {
        var bp = 0f
        bonusitems?.forEach { bp += build.items.get(it.first.item) }
        return bp / bonusitems?.size!!
    }
    fun vf():Boolean {
       var qz = 0.00f
        bonusitems?.forEach {
            qz += it.second
        }
       return qz == 1f
    }
    fun bug(log: String) {
        Log.info(log)
        while (true) {
            BaseDialog("[red]喜报: 你Mod炸").apply {
                cont.apply {
                    add("[red]喜报: 你Mod炸了\n快去看看日志").row()
                }
                show()
            }
        }
    }
    init {
        if (bonusitems == null) {
            bug("初始化时bonusitems不应为null,你这和用GenericCrafter有区别吗?")
        } else if (vf()) {
            bug("权重和≠100%")
        }
    }
    override fun setBars() {
        super.setBars()
        addBar("效率") { bounds: Building -> Bar("效率: " + (getBonus(BonusCrafterBuild()) / bounds.block.itemCapacity) * 100 + "%" , Pal.lightOrange) { (getBonus(BonusCrafterBuild()) / bounds.block.itemCapacity) } }
    }
    open inner class BonusCrafterBuild : Building() {
        val bonusCrafterTime = craftTime
        override fun update() {
            super.update()
            if (bonusCrafterTime <= getBonus(BonusCrafterBuild()) / this@BonusCrafter.itemCapacity) {
                bug("默认生产耗时 <= 加成时间")
            } else {
                craftTime = bonusCrafterTime - getBonus(BonusCrafterBuild()) / this@BonusCrafter.itemCapacity / 2 * bonus
            }
        }
    }
}
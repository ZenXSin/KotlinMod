package example

import arc.graphics.Color
import mindustry.type.Item

private var 天临协议: Item? = null
object Item {
    @JvmStatic
        fun load() {
        天临协议 = object : Item("天临协议", Color.red){
            init {
                alwaysUnlocked = false
                radioactivity = 0f
                explosiveness = 0f
                flammability = 0f
            }
        }
    }
}
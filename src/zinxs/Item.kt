package zinxs

import arc.graphics.Color
import mindustry.type.Item

object Item {
    var 临时协议: Item? = null
    @JvmStatic
        fun load() {
        临时协议 = object : Item("临时协议", Color.red){
            init {
                alwaysUnlocked = false
            }
        }
    }
}
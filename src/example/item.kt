package example

import mindustry.type.Item

private var a: Item? = null
object Item {
    @JvmStatic
        fun load() {
            a = Item("a")
    }
}
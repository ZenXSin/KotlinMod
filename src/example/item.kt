package example

import mindustry.type.Item

object Item {
        private var a: Item? = null
        fun load() {
            a = Item("a")
        }
}
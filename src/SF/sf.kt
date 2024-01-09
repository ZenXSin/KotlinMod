package SF

import SF.content.test
import arc.Core
import arc.Events
import arc.util.Log
import arc.util.Time
import mindustry.core.UI
import mindustry.game.EventType.ClientLoadEvent
import mindustry.mod.Mod
import mindustry.ui.dialogs.BaseDialog

class Ui: UI() {
    init {
        super.init()
    }
}
class sf : Mod() {
    override fun loadContent() {
        super.loadContent()
        //test.load()
    }
}


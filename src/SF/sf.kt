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
        test.load()
        Log.info("Saturation firepower By-RA2.EXE")
    }

    init {
        Events.on(ClientLoadEvent::class.java) {
            Time.runTask(10f) {
                home()
            }
        }
    }

    fun home() {
        BaseDialog("[yellow]欢迎使用本依赖脚本").apply {
            cont.apply {
                image(Core.atlas.find("lib-logo")).pad(20f).top()
                button("[orange]退出") { hide() }.size(100f, 50f)
                button("[orange]更新日志（开发中）") { hide() }.size(100f, 50f)
                button("[orange]更新（开发中）") { hide() }.size(100f, 50f)
            }
            show()
        }
    }
}


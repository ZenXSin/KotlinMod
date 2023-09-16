package SF

import SF.content.*
import SF.ilbs.*
import mindustry.mod.Mod
import arc.*
import arc.util.*
import mindustry.game.EventType.*
import mindustry.ui.dialogs.*

class Sf : Mod() {
    override fun loadContent() {
        Block.load()
        Items.load()
        Liquids.load()
        Status.load()
        Units.load()
        Weathers.load()
        TechTree.load()
        Log.info("饱和火力,启动！")
    }
    init{
        Events.on(ClientLoadEvent::class.java){
            Time.runTask(10f){
                home()
            }
        }
    }
}

fun home() {
    BaseDialog("Wellcome to [red][Saturation firepower]").apply{
        cont.apply{
            image(Core.atlas.find("饱和火力-logo")).pad(20f).row()
            add("欢迎来到饱和火力！").row()
            button("更新日志"){ update() }.size(100f, 50f)
            button("转型日志"){ hide() }.size(100f, 50f)
            button("退出"){ hide() }.size(100f, 50f)
        }
        show()
    }
}

fun update() {
    BaseDialog("更新日志").apply{
        cont.apply{
            add("这里写日志").row()
            button("返回"){ home() }.size(100f, 50f)
        }
        show()
    }
}
package example

import arc.Core
import arc.Events
import arc.util.Log
import arc.util.Time
import mindustry.game.EventType.ClientLoadEvent
import mindustry.mod.Mod
import mindustry.ui.dialogs.BaseDialog

class ExampleJavaMod : Mod() {
    init {
        Log.info("Loaded ExampleJavaMod constructor.")

        //listen for game load event
        Events.on(ClientLoadEvent::class.java) { e: ClientLoadEvent? ->
            //show dialog upon startup
            Time.runTask(10f) {
                val dialog = BaseDialog("frog")
                dialog.cont.add("behold").row()
                //mod sprites are prefixed with the mod name (this mod is called 'example-java-mod' in its config)
                dialog.cont.image(Core.atlas.find("example-kotlin-mod-frog")).pad(20f).row()
                dialog.cont.button("I see") { dialog.hide() }.size(100f, 50f)
                dialog.show()
            }
        }
    }

    override fun loadContent() {
        Log.info("Loading some example content.")
    }
}


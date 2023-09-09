package zinxs

import mindustry.world.draw.*

object Draw {
    @JvmStatic
    var drawer: DrawMulti? = null
    fun GCBuild(sinMag: Float, sinScl: Float, lenOffset:Float, sides:Int, sideOffset:Float, scale:Float? = null):DrawMulti {
        drawer = object :DrawMulti(){
            init {
                DrawRegion("-bottom")
                DrawPistons().sinMag = sinMag
                DrawPistons().sinScl = sinScl
                DrawPistons().lenOffset = lenOffset
                DrawPistons().sides = sides
                DrawPistons().sideOffset = sideOffset
                DrawDefault()
                if (scale != null){
                    DrawFade().scale = scale
                }
                DrawRegion("-top")
            }
        }
        return drawer!!
    }
}
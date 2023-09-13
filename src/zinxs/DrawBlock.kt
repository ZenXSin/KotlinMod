package zinxs

import mindustry.world.draw.*

object Draw {
    @JvmStatic
    var drawer: DrawMulti? = null
    fun build(
        SinMag: Float,
        SinScl: Float,
        LenOffset: Float,
        Sides: Int,
        SideOffset: Float,
        Scale: Float? = null
    ): DrawMulti {
        drawer = DrawMulti(
            DrawRegion("-bottom"),
            object : DrawPistons() {
                init {
                    sinMag = SinMag
                    sinScl = SinScl
                    lenOffset = LenOffset
                    sides = Sides
                    sideOffset = SideOffset
                }
            },
            DrawDefault(),
                object : DrawFade() {
                    init {
                        if (Scale != null) {
                            suffix = "-top"
                            scale = Scale
                        }
                    }
                }
        )
        return drawer!!
    }
}
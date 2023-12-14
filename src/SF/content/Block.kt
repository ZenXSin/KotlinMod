package SF.content

import SF.ilbs.BonusCrafter
import arc.func.Prov
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import mindustry.Vars
import mindustry.ctype.ContentType
import mindustry.game.Team
import mindustry.graphics.Layer
import mindustry.world.Block
import mindustry.world.Tile
import mindustry.world.blocks.storage.CoreBlock
object test {

}
/*
object test {
    var 激发放射塔 : BonusCrafter? = null
    var 前哨基地 : 前哨? = null
    open class 前哨(name: String?) : CoreBlock(name) {
        override fun canBreak(tile: Tile?): Boolean {
            super.canBreak(tile)
            return Vars.state.teams.cores(tile!!.team()).size > 1
        }

        override fun canReplace(other: Block?): Boolean {
            super.canReplace(other)
            return other!!.alwaysReplace
        }

        override fun canPlaceOn(tile: Tile?, team: Team?, rotation: Int): Boolean {
            super.canPlaceOn(tile, team, rotation)
            return Vars.state.teams.cores(team).size < 10
        }
        open inner class 前哨Build: CoreBuild() {
            var time = 60
            var kill = false
            override fun update() {
                super.update()
                if (Vars.state.teams.cores(this.team).size >= 6) {
                    Vars.ui.showLabel(
                        "[red]     数据上行堵塞\n▲中央数据库过载▲\n     强制重启倒计时",
                        0.015F, this.x, this.y
                    )
                    time--
                } else {
                    kill = true
                }
                if (time <= 0) {
                    this.kill()
                }
            }

            override fun draw() {
                super.draw()
                Draw.z(Layer.effect)
                Lines.stroke(2f, Color.valueOf("FF5B5B"))
                Draw.alpha(if (kill) 1f else if (Vars.state.teams.cores(this.team).size > 8) 1f else 0f)
                Lines.arc(this.x, this.y, 16f, (time * (6 / 360)).toFloat(), 90f)
            }

            override fun kill() {
                this.core().items().each { item, amount ->
                    if (amount > 2000) {
                        this.items.add(item, 2000 - amount)
                    }
                }
                super.kill()
            }
        }
    }
    @JvmStatic
    fun load() {
        激发放射塔 = object : BonusCrafter("激发放射塔") {
            init {
                rotate = false
                solid = true
                bonus = 5
                bonusItem = Vars.content.getByName(ContentType.item, "饱和火力-镄")
                heatItem = Vars.content.getByName(ContentType.item, "饱和火力-镄")
                heatLiquids = Vars.content.getByName(ContentType.liquid, "饱和火力-纳米流体")
                heatd = 10f
            }
        }
        前哨基地 = object : 前哨("前哨基地") {
            init {
                rotate = false
                solid = true
                update = true
            }
        }
    }
    }*/
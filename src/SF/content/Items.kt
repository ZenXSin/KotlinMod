package SF.content

import arc.graphics.Color
import mindustry.type.Item

object Items {
    var 一级协议: Item? = null
    var 二级协议: Item? = null
    var 三级协议: Item? = null
    var 水桶: Item? = null
    var 冷冻液桶: Item? = null
    var 石油桶: Item? = null
    var 矿渣桶: Item? = null
    var 纳米流体桶: Item? = null
    var 镄: Item? = null
    var 铬: Item? = null
    var 集束: Item? = null
    var 裂位能: Item? = null
    var 硅钢: Item? = null
    var 纳米核: Item? = null
    var 莱普合金: Item? = null
    var 泰勒合金: Item? = null
    @JvmStatic
    fun load() {
        一级协议 = object : Item("初级协议"){
            init {
                hardness = 0
                cost = 0.5f
                color = Color.valueOf("00EE00")
                alwaysUnlocked = false
                radioactivity =  0f
                explosiveness =  0f
                flammability =  0f
                frames = 6
                transitionFrames = 1
                frameTime = 3f
                //research =  "titanium"
            }
        }
        裂位能 = object : Item("裂位能") {
            init {
                cost = 6f
                hardness = 1
                alwaysUnlocked = false
                color = Color.valueOf("EEC591")
                healthScaling = 0.9f
                radioactivity = 2.5f
                explosiveness = 0.85f
                flammability = 0f
                frames = 10
                transitionFrames = 3
                frameTime = 3f
             //   research = phase-fabric
            }
        }
    }
}
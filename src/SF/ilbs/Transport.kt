package SF.ilbs

import mindustry.content.StatusEffects
import mindustry.gen.Unit
import mindustry.gen.WaterMovec
import mindustry.type.StatusEffect
import mindustry.type.UnitType
import javax.persistence.Entity

@Entity
abstract class TransportUnit(name:String) : UnitType(name), WaterMovec {
    override fun update(unit: Unit?) {
        super.update(unit)
    }
    // 添加需要持久化的属性和方法
}


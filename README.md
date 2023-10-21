ps:自己用来实现一些和mod有/无关的东西，里的接口用js写就能用了
# Libs
* 扩展接口声明：
* 原作@Anuken
* 扩展@ZenXS
* 示例项目：content/block
* Js,Java,Kotlin可用
* 功能:
* 1.完全支持原版@GenericCrafter的所有功能
* 2.扩展功能如下（所有功能皆为可选）：
* (1) 动态craftTime
* 计算方式：
* craftTime = craftTime - bonusItem数量/2 * Bonus
* bonusItem: 加速物品
* Bonus: 加速倍率
* 当craftTime <= 0 时， 设置 craftTime = 10
* (2)类似钍反的的热量系统
* 热量增加方式：
* heat += 0.01 * heatItem数量 / heatd
* heatItem: 反应物品
* heatd: 热容量
* 爆炸特效需自己写

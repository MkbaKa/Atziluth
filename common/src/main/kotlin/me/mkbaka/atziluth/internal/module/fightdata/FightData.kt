package me.mkbaka.atziluth.internal.module.fightdata

import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

/**
 * 处理战斗过程的数据
 */
interface FightData {

    /**
     * 攻击者
     */
    val attacker: LivingEntity

    /**
     * 受击者
     */
    val entity: LivingEntity

    /**
     * 要触发的属性列表
     */
    val attributes: MutableList<CustomAttribute>

    /**
     * 存储战斗过程中的属性数据
     */
    val attributeData: MutableMap<UUID, MutableMap<String, DoubleArray>>

    /**
     * 伤害事件
     */
    val event: EntityDamageByEntityEvent

    /**
     * 伤害值
     */
    var damageValue: Double

    /**
     * 蓄力程度
     */
    val force: Double

    /**
     * 取消此次伤害处理
     */
    var isCancelled: Boolean

}
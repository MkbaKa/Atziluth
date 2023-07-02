package me.mkbaka.atziluth.internal.register

import org.bukkit.entity.LivingEntity

/**
 * 不同属性插件之间方法的桥接
 */
interface BaseAttribute {

    fun getFinalDamage(attacker: LivingEntity): Double

    fun addFinalDamage(attacker: LivingEntity, value: Double)

    fun takeFinalDamage(attacker: LivingEntity, value: Double)

    fun setFinalDamage(attacker: LivingEntity, value: Double)

    val isProjectile: Boolean

}
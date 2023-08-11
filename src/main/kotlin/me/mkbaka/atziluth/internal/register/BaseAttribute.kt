package me.mkbaka.atziluth.internal.register

import org.bukkit.entity.LivingEntity

/**
 * 不同属性插件之间方法的桥接
 */

interface BaseAttribute<T> {

    val isProjectile: Boolean

    fun getFinalDamage(attacker: LivingEntity, handle: T): Double

    fun addFinalDamage(attacker: LivingEntity, value: Double, handle: T)

    fun takeFinalDamage(attacker: LivingEntity, value: Double, handle: T)

    fun setFinalDamage(attacker: LivingEntity, value: Double, handle: T)

}
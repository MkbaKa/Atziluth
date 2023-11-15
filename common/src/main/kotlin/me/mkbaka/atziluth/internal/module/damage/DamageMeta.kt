package me.mkbaka.atziluth.internal.module.damage

import org.bukkit.entity.LivingEntity

/**
 * 伤害元
 */
interface DamageMeta {

    /**
     * 攻击者
     */
    val damager: LivingEntity

    /**
     * 受击者
     */
    val entities: Collection<LivingEntity>

    /**
     * 伤害选项
     */
    val options: DamageOptions

    /**
     * 基于当前的 DamageMeta 与 DamageOptions 造成伤害
     */
    fun doDamage()

}
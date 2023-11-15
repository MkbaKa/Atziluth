package me.mkbaka.atziluth.internal.module.damage

/**
 * 伤害参数
 */
interface DamageOptions {

    /**
     * 基础伤害值
     */
    val damageValue: Double

    /**
     * 造成伤害时不造成击退
     */
    val preventKnockback: Boolean

    /**
     * 造成伤害时无视无敌帧
     */
    val ignoreImmunity: Boolean

    /**
     * 造成伤害后的无敌帧
     */
    val noDamageTicks: Int

}
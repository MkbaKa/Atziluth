package me.mkbaka.atziluth.internal.module.damage

interface DamageOptions {

    val damageValue: Double

    val preventKnockback: Boolean

    val ignoreImmunity: Boolean

    val noDamageTicks: Int

}
package me.mkbaka.atziluth.internal.utils.damage.impl

import me.mkbaka.atziluth.internal.utils.damage.DamageOptions

open class BasicDamageOptions(
    override var basicDamageValue: Double = 1.0,
    override var noDamageTicks: Int = 20,
    override var preventKnockback: Boolean = false,
    override var ignoreImmunity: Boolean = false
) : DamageOptions
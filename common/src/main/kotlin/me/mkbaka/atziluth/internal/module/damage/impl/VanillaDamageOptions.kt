package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.internal.module.damage.DamageOptions

open class VanillaDamageOptions(
    override val damageValue: Double,
    override val preventKnockback: Boolean,
    override val ignoreImmunity: Boolean,
    override val noDamageTicks: Int
) : DamageOptions
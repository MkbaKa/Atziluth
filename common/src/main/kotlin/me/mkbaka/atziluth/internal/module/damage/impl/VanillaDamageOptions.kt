package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.internal.module.damage.DamageOptions

open class VanillaDamageOptions(
    override var damageValue: Double,
    override var preventKnockback: Boolean,
    override var ignoreImmunity: Boolean,
    override var noDamageTicks: Int
) : DamageOptions
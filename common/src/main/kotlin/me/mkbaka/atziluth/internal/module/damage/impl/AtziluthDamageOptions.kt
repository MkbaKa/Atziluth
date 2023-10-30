package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData

class AtziluthDamageOptions(
    damageValue: Double,
    preventKnockback: Boolean,
    ignoreImmunity: Boolean,
    noDamageTicks: Int,
    val attributes: TempAttributeData,
    val clearAttribute: Boolean = false,
    val whiteListAttribute: List<String> = emptyList()
) : VanillaDamageOptions(damageValue, preventKnockback, ignoreImmunity, noDamageTicks)
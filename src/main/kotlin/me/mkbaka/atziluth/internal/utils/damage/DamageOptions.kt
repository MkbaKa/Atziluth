package me.mkbaka.atziluth.internal.utils.damage

import me.mkbaka.atziluth.internal.utils.damage.impl.BasicDamageOptions

interface DamageOptions {

    var basicDamageValue: Double

    var noDamageTicks: Int

    companion object {

        fun new(constructor: DamageOptions.() -> Unit): DamageOptions {
            return BasicDamageOptions().also(constructor)
        }

    }

}
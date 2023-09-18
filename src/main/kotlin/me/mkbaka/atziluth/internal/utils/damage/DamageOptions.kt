package me.mkbaka.atziluth.internal.utils.damage

import me.mkbaka.atziluth.internal.utils.damage.impl.BasicDamageOptions

interface DamageOptions {

    // 基础伤害值
    var basicDamageValue: Double

    // 造成伤害后后的无敌帧时间
    var noDamageTicks: Int

    // 造成伤害后是否取消击退
    var preventKnockback: Boolean

    // 造成伤害前是否无视无敌帧
    var ignoreImmunity: Boolean

    open class DamageOptionsBuilder {

        protected open val options = BasicDamageOptions()

        var basicDamageValue: Double
            get() = options.basicDamageValue
            set(value) {
                options.basicDamageValue = value
            }

        var noDamageTicks: Int
            get() = options.noDamageTicks
            set(value) {
                options.noDamageTicks = value
            }

        var isPreventKnockback: Boolean
            get() = options.preventKnockback
            set(value) {
                options.preventKnockback = value
            }

        var isIgnoreImmunity: Boolean
            get() = options.ignoreImmunity
            set(value) {
                options.ignoreImmunity = value
            }

        fun build(): DamageOptions {
            return options
        }

    }

    companion object {

        fun new(constructor: DamageOptionsBuilder.() -> Unit): DamageOptions {
            return DamageOptionsBuilder().also(constructor).build()
        }

    }

}
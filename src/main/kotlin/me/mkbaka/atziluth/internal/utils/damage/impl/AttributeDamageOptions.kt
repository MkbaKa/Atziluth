package me.mkbaka.atziluth.internal.utils.damage.impl

import me.mkbaka.atziluth.internal.utils.damage.DamageOptions
import java.util.*

class AttributeDamageOptions(
    // 属性源
    var source: String = UUID.randomUUID().toString(),
    // 若clear为true 白名单中的属性不会被清除
    val whitelistAttributes: MutableList<String> = mutableListOf(),
    // 要触发的属性 String中需包含属性名和数值
    val attributes: MutableList<String> = mutableListOf(),
    // 是否清除玩家原有属性再造成伤害
    var clear: Boolean = false
) : BasicDamageOptions(1.0) {

    class AttributeDamageOptionsBuilder : DamageOptions.DamageOptionsBuilder() {

        override val options = AttributeDamageOptions()

        fun setAttributeSource(source: String) {
            options.source = source
        }

        fun setWhitelistAttr(attrNames: List<String>) {
            options.whitelistAttributes.clear()
            options.whitelistAttributes.addAll(attrNames)
        }

        fun setAttribute(nameAndValues: List<String>) {
            options.attributes.clear()
            options.attributes.addAll(nameAndValues)
        }

        fun setAttribute(attrs: Map<String, Double>) {
            options.attributes.clear()
            options.attributes.addAll(attrs.map { entry -> "${entry.key}: ${entry.value}" })
        }

        var isClear: Boolean
            get() = options.clear
            set(value) {
                options.clear = value
            }

    }

    companion object {

        fun new(constructor: AttributeDamageOptionsBuilder.() -> Unit): DamageOptions {
            return AttributeDamageOptionsBuilder().also(constructor).build()
        }

    }

}
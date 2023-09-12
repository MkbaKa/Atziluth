package me.mkbaka.atziluth.internal.utils.damage.impl

import me.mkbaka.atziluth.internal.utils.damage.DamageOptions
import java.util.*

class AttributeDamageOptions(
    basicDamageValue: Double = 1.0,
    var source: String = UUID.randomUUID().toString(),
    val whitelistAttributes: MutableList<String> = mutableListOf(),
    val attributes: MutableList<String> = mutableListOf(),
    var clear: Boolean = false
) : BasicDamageOptions(basicDamageValue) {

    class OptionsBuilder {

        private val options = AttributeDamageOptions()

        fun setDamageValue(value: Double) {
            options.basicDamageValue = value
        }

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

        fun setClear(clear: Boolean) {
            options.clear = clear
        }

        fun build(): DamageOptions {
            return options
        }

    }

    companion object {

        fun new(constructor: OptionsBuilder.() -> Unit): DamageOptions {
            return OptionsBuilder().also(constructor).build()
        }

    }

}
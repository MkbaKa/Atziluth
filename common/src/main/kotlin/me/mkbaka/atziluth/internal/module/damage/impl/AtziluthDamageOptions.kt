package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.utils.AttributeUtil.append

class AtziluthDamageOptions(
    damageValue: Double = 1.0,
    preventKnockback: Boolean = false,
    ignoreImmunity: Boolean = false,
    noDamageTicks: Int = 20,
    var clearAttribute: Boolean = false,
    val attributes: MutableMap<String, DoubleArray> = hashMapOf(),
    val whiteListAttribute: MutableList<String> = mutableListOf()
) : VanillaDamageOptions(damageValue, preventKnockback, ignoreImmunity, noDamageTicks) {

    class AtziluthOptionsBuilder {

        private val options = AtziluthDamageOptions()

        var damageValue: Double
            get() = options.damageValue
            set(value) {
                options.damageValue = value
            }

        var preventKnockback: Boolean
            get() = options.preventKnockback
            set(value) {
                options.preventKnockback = value
            }

        var ignoreImmunity: Boolean
            get() = options.ignoreImmunity
            set(value) {
                options.ignoreImmunity = value
            }

        var noDamageTicks: Int
            get() = options.noDamageTicks
            set(value) {
                options.noDamageTicks = value
            }

        var isClear: Boolean
            get() = options.clearAttribute
            set(value) {
                options.clearAttribute = value
            }

        fun setAttributes(map: Map<String, DoubleArray>) {
            options.attributes.clear()
            options.attributes.putAll(map)
        }

        fun addAttributes(map: Map<String, DoubleArray>) {
            map.forEach { (name, values) ->
                options.attributes.compute(name) { _, oldValue ->
                    (oldValue ?: doubleArrayOf(0.0, 0.0)).append(values)
                }
            }
        }

        fun removeAttribute(name: String) {
            options.attributes.remove(name)
        }

        fun setWhitelistAttributeNames(names: List<String>) {
            options.whiteListAttribute.clear()
            options.whiteListAttribute.addAll(names)
        }

        fun addWhitelistAttributeNames(names: List<String>) {
            options.whiteListAttribute.addAll(names)
        }

        fun removeWhitelistAttributeName(name: String) {
            options.whiteListAttribute.remove(name)
        }

        fun build(): AtziluthDamageOptions {
            return options
        }

    }

    companion object {

        fun new(constructor: AtziluthOptionsBuilder.() -> Unit): AtziluthDamageOptions {
            return AtziluthOptionsBuilder().also(constructor).build()
        }

    }

}
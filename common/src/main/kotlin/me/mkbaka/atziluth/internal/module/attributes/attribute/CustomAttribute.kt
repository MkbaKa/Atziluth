package me.mkbaka.atziluth.internal.module.attributes.attribute

import me.mkbaka.atziluth.internal.module.attributes.attribute.impl.CustomAttributeImpl
import org.bukkit.entity.LivingEntity

interface CustomAttribute {

    val attributeName: String

    val attributeType: CustomAttributeType

    var isBefore: Boolean

    var priority: Int

    val placeholder: String

    var combatPower: Double

    var period: Int

    var skipFilter: Boolean

    var onLoad: (CustomAttribute) -> Unit

    var callback: (LivingEntity, LivingEntity, CustomAttribute, Map<String, Any>) -> Unit

    var run: (LivingEntity, CustomAttribute) -> Unit

    companion object {

        fun buildAttribute(name: String, type: CustomAttributeType, placeholder: String, callback: CustomAttribute.() -> Unit): CustomAttribute {
            return CustomAttributeImpl(name, type, placeholder).also(callback)
        }

    }

}
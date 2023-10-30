package me.mkbaka.atziluth.internal.module.attributes.attribute.impl

import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import org.bukkit.entity.LivingEntity

class CustomAttributeImpl(
    override val attributeName: String,
    override val attributeType: CustomAttributeType,
    override val placeholder: String
) : CustomAttribute {

    override var isBefore: Boolean = false

    override var priority: Int = -1

    override var period: Int = -1

    override var combatPower: Double = 1.0

    override var skipFilter: Boolean = false

    override var onLoad: (CustomAttribute) -> Unit = {}

    override var callback: (LivingEntity, LivingEntity, CustomAttribute, Map<String, Any>) -> Unit = { _, _, _, _ -> }

    override var run: (LivingEntity, CustomAttribute) -> Unit = { _, _ -> }

}
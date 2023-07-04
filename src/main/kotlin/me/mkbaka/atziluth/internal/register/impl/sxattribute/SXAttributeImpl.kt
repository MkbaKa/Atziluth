package me.mkbaka.atziluth.internal.register.impl.sxattribute

import github.saukiya.sxattribute.SXAttribute
import github.saukiya.sxattribute.data.attribute.SubAttribute
import me.mkbaka.atziluth.internal.register.AttributeFactory
import me.mkbaka.atziluth.internal.register.AttributeType

object SXAttributeImpl : AttributeFactory<SXAttributeAdapter>() {

    override fun reload() {
        SubAttribute.getAttributes().removeIf { it.javaClass.`package`.name == packageName }
    }

    override fun buildAttribute(
        priority: Int,
        name: String,
        placeholder: String,
        combatPower: Double,
        type: AttributeType
    ): SXAttributeAdapter {
        return SXAttributeAdapter(priority, name, placeholder, type)
    }

    override fun isAttribute(name: String): Boolean {
        return SubAttribute.getAttributes().firstOrNull { it.name == name } != null
    }

    override fun registerAttribute(customAttribute: SXAttributeAdapter) {
        if (customAttribute.attrPriority <= -1) {
            SXAttribute.getInst().logger.warning("Attribute >> Disable [§9Atzi§3luth§r|§c${customAttribute.name}§r] !");
            return
        }
        customAttribute.inst.apply {
            SubAttribute.getAttributes().add(this.priority, this)
            SXAttribute.getInst().logger
                .info("Attribute >> Register [§9Atzi§3luth§r|§a${name}§r] To Priority $priority !")
        }
    }

    override fun registeredCallback() {
        SXAttribute.getAttributeManager().loadDefaultAttributeData()
    }

}
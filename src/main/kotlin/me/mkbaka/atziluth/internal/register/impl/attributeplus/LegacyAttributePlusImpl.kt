package me.mkbaka.atziluth.internal.register.impl.attributeplus

import me.mkbaka.atziluth.internal.register.AttributeFactory
import me.mkbaka.atziluth.internal.register.AttributeType
import org.serverct.ersha.jd.Main

object LegacyAttributePlusImpl : AttributeFactory<LegacyAttributePlusAdapter>() {

    private val manager by lazy { Main.getAttributeManager() }

    override fun registerAttribute(customAttribute: LegacyAttributePlusAdapter) {
        customAttribute.inst.registerAttribute()
    }

    override fun reload() {
        manager.attribute.entries.removeIf { it.value.javaClass.`package`.name == packageName }
    }

    override fun buildAttribute(
        priority: Int,
        name: String,
        placeholder: String,
        combatPower: Double,
        type: AttributeType
    ): LegacyAttributePlusAdapter {
        return buildAttribute(name, placeholder, type)
    }

    override fun isAttribute(name: String): Boolean {
        return manager.attribute.containsKey(name)
    }

    override fun buildAttribute(
        name: String,
        placeholder: String,
        type: AttributeType
    ): LegacyAttributePlusAdapter {
        return LegacyAttributePlusAdapter(name, placeholder, type)
    }

    override fun getAllAttributeNames(): Collection<String> {
        return manager.attributeName.map { it.value }
    }

}
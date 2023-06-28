package me.mkbaka.atziluth

import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.bridge.impl.attributeplus.AttributePlusBridge
import me.mkbaka.atziluth.internal.bridge.impl.attributeplus.LegacyAttributePlusBridge
import me.mkbaka.atziluth.internal.bridge.impl.sxattribute.SXAttributeBridge
import me.mkbaka.atziluth.internal.register.AttributeFactory
import me.mkbaka.atziluth.internal.register.impl.attributeplus.AttributePlusImpl
import me.mkbaka.atziluth.internal.register.impl.attributeplus.LegacyAttributePlusImpl
import me.mkbaka.atziluth.internal.register.impl.sxattribute.SXAttributeImpl
import me.mkbaka.atziluth.internal.utils.Util.version
import org.bukkit.plugin.Plugin

enum class AttributePlugin(
    val pluginName: String,
    val impl: AttributeFactory<*>,
    val bridge: AttributeBridge,
    val condition: (Plugin) -> Boolean
) {

    ATTRIBUTE_PLUS("AttributePlus", AttributePlusImpl, AttributePlusBridge, { it.version == 3 }),
    SX_ATTRIBUTE("SX-Attribute", SXAttributeImpl, SXAttributeBridge, { it.version == 3 }),
    LEGACY_ATTRIBUTE_PLUS("AttributePlus", LegacyAttributePlusImpl, LegacyAttributePlusBridge, { it.version == 2 });
//    ORIGIN_ATTRIBUTE("OriginAttribute", OriginAttributeImpl, { true });

    /**
     * 设计过于先进 非常人可理解
     * 个人技术力不足 有能力可以自行尝试
     *
     * LEGACY_SX_ATTRIBUTE("SX-Attribute", LegacySXAttributeImpl, { it.version == 2 }),
     */

    companion object {

        fun of(name: String): AttributePlugin? {
            return values().firstOrNull {
                name.equals(it.name, ignoreCase = true) || name.equals(it.pluginName, ignoreCase = true)
            }
        }

    }
}
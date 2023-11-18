package me.mkbaka.atziluth.internal.module.attributes.impl.sxattribute.v3

import github.saukiya.sxattribute.data.attribute.AttributeType
import github.saukiya.sxattribute.data.attribute.SubAttribute
import github.saukiya.sxattribute.data.eventdata.EventData
import github.saukiya.sxattribute.event.SXLoadAttributeEvent
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.module.attributes.AttributeDataManager
import me.mkbaka.atziluth.internal.module.attributes.AttributePluginHooker
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import org.bukkit.entity.Player
import taboolib.common5.eqic

class SXAttributeHookerImpl : AttributePluginHooker<SXLoadAttributeEvent, SXLoadAttributeEvent>() {

    override val attributeDataManager: AttributeDataManager<*>
        get() = SXAttributeDataManager

    override val beforeUpdateEvent: Class<SXLoadAttributeEvent>
        get() = SXLoadAttributeEvent::class.java

    override val afterUpdateEvent: Class<SXLoadAttributeEvent>
        get() = SXLoadAttributeEvent::class.java

    override fun handleUpdateBefore(event: SXLoadAttributeEvent, attrs: Collection<CustomAttribute>) {
        val entity = event.entity
        attrs.forEach { attr ->
            if (attr.skipFilter || entity.getAttrValue(attr.attributeName) > 0.0) {
                attr.run(entity)
            }
        }
    }

    override fun handleUpdateAfter(event: SXLoadAttributeEvent, attrs: Collection<CustomAttribute>) {
        error("当前属性插件不支持 UpdateAfter 类型")
    }

    override fun registerOtherAttribute(name: String, combatPower: Double, placeholder: String) {
        SubAttribute.getAttributes().add(buildOtherAttribute(name, placeholder))
    }

    override fun getAllAttributes(whiteListAttribute: List<String>): Collection<String> {
        return SubAttribute.getAttributes().map { it.name }
    }

    override fun reload() {

    }

    private fun buildOtherAttribute(name: String, placeholder: String): SubAttribute {
        return object : SubAttribute(name, Atziluth.plugin, 2, AttributeType.OTHER) {
            override fun eventMethod(values: DoubleArray, data: EventData) {}

            override fun getPlaceholder(values: DoubleArray, player: Player, str: String): Any? {
                return if (str.eqic(placeholder)) {
                    if (values[1] != 0.0) "${values[0]} - ${values[1]}" else values[0]
                } else null
            }

            override fun getPlaceholders(): MutableList<String> {
                return mutableListOf(name)
            }

            override fun loadAttribute(values: DoubleArray, lore: String) {
                if (lore.contains(name)) {
                    if (lore.contains("-")) {
                        val splits = lore.split("-")
                        splits.forEachIndexed { index, line ->
                            values[index] = getNumber(line)
                        }
                    } else {
                        values[0] = getNumber(lore)
                    }
                }
            }
        }
    }

}
package me.mkbaka.atziluth.internal.module.attributes.impl.attributeplus.v3

import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.module.attributes.AttributePluginHooker
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeDataManager
import org.serverct.ersha.api.event.AttrUpdateAttributeEvent
import org.serverct.ersha.script.AttrScriptUtils

class AttributePlusHookerImpl : AttributePluginHooker<AttrUpdateAttributeEvent.Before, AttrUpdateAttributeEvent.After>() {

    override val attributeDataManager: AttributeDataManager<*>
        get() = AttributePlusDataManager

    override val beforeUpdateEvent: Class<AttrUpdateAttributeEvent.Before>
        get() = AttrUpdateAttributeEvent.Before::class.java

    override val afterUpdateEvent: Class<AttrUpdateAttributeEvent.After>
        get() = AttrUpdateAttributeEvent.After::class.java

    override fun handleUpdateBefore(event: AttrUpdateAttributeEvent.Before, attrs: Collection<CustomAttribute>) {
        val entity = event.attributeData.sourceEntity
        attrs.forEach { attr ->
            if (attr.skipFilter || entity.getAttrValue(attr.attributeName) > 0.0) {
                attr.run(event.attributeData.sourceEntity, attr)
            }
        }
    }

    override fun handleUpdateAfter(event: AttrUpdateAttributeEvent.After, attrs: Collection<CustomAttribute>) {
        val entity = event.attributeData.sourceEntity
        attrs.forEach { attr ->
            if (attr.skipFilter || entity.getAttrValue(attr.attributeName) > 0.0) {
                attr.run(event.attributeData.sourceEntity, attr)
            }
        }
    }

    override fun registerOtherAttribute(name: String, combatPower: Double, placeholder: String) {
        AttrScriptUtils.registerOtherAttribute(name, combatPower, placeholder)
    }

    override fun reload() {

    }

}
package me.mkbaka.atziluth.internal.module.attributes.impl.attributeplus.v2

import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.module.attributes.AttributePluginHooker
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeDataManager
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.jd.api.AttributeType
import org.serverct.ersha.jd.api.BaseAttribute
import org.serverct.ersha.jd.event.AttrAttributeUpdateEvent

class AttributePlusHookerImpl : AttributePluginHooker<AttrAttributeUpdateEvent, AttrAttributeUpdateEvent>() {

    override val attributeDataManager: AttributeDataManager<*>
        get() = AttributePlusDataManager

    override val beforeUpdateEvent: Class<AttrAttributeUpdateEvent>
        get() = AttrAttributeUpdateEvent::class.java

    override val afterUpdateEvent: Class<AttrAttributeUpdateEvent>
        get() = AttrAttributeUpdateEvent::class.java

    override fun handleUpdateBefore(event: AttrAttributeUpdateEvent, attrs: Collection<CustomAttribute>) {
        val entity = event.entity as? LivingEntity ?: return
        attrs.forEach { attr ->
            if (attr.skipFilter || entity.getAttrValue(attr.attributeName) > 0.0) {
                attr.run(entity, attr)
            }
        }
    }

    override fun handleUpdateAfter(event: AttrAttributeUpdateEvent, attrs: Collection<CustomAttribute>) {
        error("当前属性插件不支持 UpdateAfter 类型")
    }

    override fun registerOtherAttribute(name: String, combatPower: Double, placeholder: String) {
        object : BaseAttribute(AttributeType.NULL, name, placeholder) { }.registerAttribute()
    }

    override fun reload() {

    }

}
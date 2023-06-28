package me.mkbaka.atziluth.internal.bridge.impl.attributeplus

import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.bridge.AttributeValueType
import me.mkbaka.atziluth.internal.utils.EntityUtil.getLivingEntity
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.jd.AttributeAPI
import org.serverct.ersha.jd.api.EntityAttributeAPI
import org.serverct.ersha.jd.attribute.AttributeData
import taboolib.common5.cdouble
import java.util.*

object LegacyAttributePlusBridge : AttributeBridge {

    override fun addAttributes(uuid: UUID, source: String, attrs: List<String>) {
        val entity = uuid.getLivingEntity() ?: return
        addAttributes(entity, source, attrs)
    }

    override fun addAttributes(entity: LivingEntity, source: String, attrs: List<String>) {
        EntityAttributeAPI.addEntityAttribute(entity, source, attrs)
    }

    override fun takeAttribute(uuid: UUID, source: String) {
        val entity = uuid.getLivingEntity() ?: return
        takeAttribute(entity, source)
    }

    override fun takeAttribute(entity: LivingEntity, source: String) {
        EntityAttributeAPI.removeEntityAttribute(entity, source)
    }

    override fun getAttrValue(uuid: UUID, attrName: String, type: AttributeValueType): Double {
        val entity = uuid.getLivingEntity() ?: return Double.NaN
        return entity.attrValue(attrName, type)
    }

    override fun getAttrValue(entity: LivingEntity, attrName: String, type: AttributeValueType): Double {
        return entity.attrValue(attrName, type)
    }

    private val LivingEntity.attrData: AttributeData
        get() = AttributeAPI.getAttrData(this)

    private fun LivingEntity.attrValue(name: String, type: AttributeValueType): Double {
        val data = this.attrData
        return when (type) {
            AttributeValueType.MIN -> data.getAttributeValues(name)[0]
            AttributeValueType.MAX -> data.getAttributeValues(name)[1]
            AttributeValueType.RANDOM -> data.getAttributeValue(name)
        }.cdouble
    }

}
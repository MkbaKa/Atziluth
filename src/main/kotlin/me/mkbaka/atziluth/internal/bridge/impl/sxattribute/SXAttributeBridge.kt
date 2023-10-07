package me.mkbaka.atziluth.internal.bridge.impl.sxattribute

import github.saukiya.sxattribute.SXAttribute
import github.saukiya.sxattribute.data.attribute.SXAttributeData
import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.bridge.AttributeValueType
import me.mkbaka.atziluth.internal.utils.EntityUtil.getLivingEntity
import org.bukkit.entity.LivingEntity
import taboolib.common.util.random
import taboolib.common5.cdouble
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object SXAttributeBridge : AttributeBridge {

    private val api by lazy { SXAttribute.getApi() }
    private val attributeSources = ConcurrentHashMap<UUID, HashMap<String, SXAttributeData>>()

    override fun addAttributes(uuid: UUID, source: String, attrs: List<String>) {
        val entity = uuid.getLivingEntity() ?: return
        addAttributes(entity, source, attrs)
    }

    override fun addAttributes(entity: LivingEntity, source: String, attrs: List<String>) {
        val uuid = entity.uniqueId
        val sources = attributeSources.getOrPut(uuid) { hashMapOf() }

        if (sources.containsKey(source)) {
            takeAttribute(entity, source)
            sources.remove(source)
        }

        val data = api.loadListData(attrs)

        sources[source] = data
        entity.attrData.add(data)
    }

    override fun takeAttribute(uuid: UUID, source: String) {
        val entity = uuid.getLivingEntity() ?: return
        takeAttribute(entity, source)
    }

    override fun takeAttribute(entity: LivingEntity, source: String) {
        val uuid = entity.uniqueId
        val sources = attributeSources.getOrPut(uuid) { hashMapOf() }

        if (sources.containsKey(source)) {
            sources.remove(source)
            entity.attrData.take(sources[source])
        }
    }

    override fun getAttrValue(uuid: UUID, attrName: String, type: AttributeValueType): Double {
        val entity = uuid.getLivingEntity() ?: return Double.NaN
        return getAttrValue(entity, attrName, type)
    }

    override fun getAttrValue(entity: LivingEntity, attrName: String, type: AttributeValueType): Double {
        val values = entity.attrData.getValues(attrName)
        return when (type) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> if (values[0] != 0.0 && values[1] == 0.0) values[0] else random(
                values[0],
                values[1]
            )
        }.cdouble
    }

    private val LivingEntity.attrData: SXAttributeData
        get() = api.getEntityData(this)

}
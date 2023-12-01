package me.mkbaka.atziluth.internal.module.attributes.impl.attributeplus.v3

import me.mkbaka.atziluth.internal.module.attributes.AttributeDataManager
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.serverct.ersha.AttributePlus
import org.serverct.ersha.api.AttributeAPI
import org.serverct.ersha.attribute.data.AttributeData
import taboolib.common.util.random
import taboolib.common5.cdouble
import java.util.*

object AttributePlusDataManager : AttributeDataManager<AttributeData> {

    private val api by lazy { AttributeAPI.getAPI() }
    private val attributeManager by lazy { AttributePlus.attributeManager }

    override fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData) {
        getData(entity)?.let { data ->
            api.addSourceAttribute(data, tempAttributeData.source, tempAttributeData.formatAttributes())
        }
    }

    override fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData) {
        getData(uuid)?.let { data ->
            api.addSourceAttribute(data, tempAttributeData.source, tempAttributeData.formatAttributes())
        }
    }

    override fun takeAttribute(entity: LivingEntity, source: String) {
        getData(entity)?.let { data ->
            api.takeSourceAttribute(data, source)
        }
    }

    override fun takeAttribute(uuid: UUID, source: String) {
        getData(uuid)?.let { data ->
            api.takeSourceAttribute(data, source)
        }
    }

    override fun getAttributeValue(entity: LivingEntity, attribute: String, valueType: AttributeValueType): Double {
        return getAttributeValue(entity.uniqueId, attribute, valueType)
    }

    override fun getAttributeValue(uuid: UUID, attribute: String, valueType: AttributeValueType): Double {
        val values = getData(uuid)?.getAttributeValue(attribute) ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> random(values[0].cdouble, values[1].cdouble)
        }.cdouble
    }

    override fun getItemAttribute(
        entity: LivingEntity,
        item: ItemStack,
        attribute: String,
        valueType: AttributeValueType
    ): Double {
        val values = api.getAttributeSource(item).attributeValue[attribute] ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> random(values[0].cdouble, values[1].cdouble)
        }.cdouble
    }

    override fun getData(entity: LivingEntity): AttributeData? {
        return attributeManager.entityAttributeData[entity.uniqueId]
    }

    override fun getData(uuid: UUID): AttributeData? {
        return attributeManager.entityAttributeData[uuid]
    }

}
package me.mkbaka.atziluth.internal.module.attributes.impl.sxattribute.v3

import github.saukiya.sxattribute.SXAttribute
import github.saukiya.sxattribute.data.PreLoadItem
import github.saukiya.sxattribute.data.attribute.SXAttributeData
import me.mkbaka.atziluth.internal.module.attributes.AttributeDataManager
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.EntityUtil.getLivingEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.common.util.random
import taboolib.common5.cdouble
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object SXAttributeDataManager : AttributeDataManager<SXAttributeData> {

    private val api by lazy { SXAttribute.getApi() }
    private val attributeSources = ConcurrentHashMap<UUID, HashMap<String, SXAttributeData>>()

    override fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData) {
        getData(entity)?.let { entityData ->
            val uuid = entity.uniqueId
            val sources = attributeSources.getOrPut(uuid) { hashMapOf() }

            if (sources.containsKey(tempAttributeData.source)) {
                takeAttribute(entity, tempAttributeData.source)
                sources.remove(tempAttributeData.source)
            }

            val data = api.loadListData(tempAttributeData.formatAttributes())

            sources[tempAttributeData.source] = data
            entityData.add(data)
        }
    }

    override fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData) {
        val entity = uuid.getLivingEntity() ?: return
        addAttribute(entity, tempAttributeData)
    }

    override fun takeAttribute(entity: LivingEntity, source: String) {
        getData(entity)?.let { entityData ->
            val uuid = entity.uniqueId
            val sources = attributeSources.getOrPut(uuid) { hashMapOf() }

            if (sources.containsKey(source)) {
                sources.remove(source)
                entityData.take(sources[source])
            }
        }
    }

    override fun takeAttribute(uuid: UUID, source: String) {
        val entity = uuid.getLivingEntity() ?: return
        takeAttribute(entity, source)
    }

    override fun getAttributeValue(entity: LivingEntity, attribute: String, valueType: AttributeValueType): Double {
        val values = getData(entity)?.getValues(attribute) ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> random(values[0].cdouble, values[1].cdouble)
        }.cdouble
    }

    override fun getAttributeValue(uuid: UUID, attribute: String, valueType: AttributeValueType): Double {
        val values = getData(uuid)?.getValues(attribute) ?: return 0.0
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
        val values = api.loadItemData(entity, PreLoadItem(item)).getValues(attribute) ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> random(values[0].cdouble, values[1].cdouble)
        }.cdouble
    }

    override fun getData(entity: LivingEntity): SXAttributeData? {
        return api.getEntityData(entity)
    }

    override fun getData(uuid: UUID): SXAttributeData? {
        return api.getEntityData(uuid.getLivingEntity()!!)
    }

}
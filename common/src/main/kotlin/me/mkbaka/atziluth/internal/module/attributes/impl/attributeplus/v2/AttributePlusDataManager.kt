package me.mkbaka.atziluth.internal.module.attributes.impl.attributeplus.v2

import me.mkbaka.atziluth.internal.module.attributes.AttributeDataManager
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.EntityUtil.getLivingEntity
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.jd.Main
import org.serverct.ersha.jd.api.EntityAttributeAPI
import org.serverct.ersha.jd.attribute.AttributeData
import taboolib.common.util.random
import taboolib.common5.cdouble
import java.util.*

object AttributePlusDataManager : AttributeDataManager<AttributeData> {

    private val attributeManager by lazy { Main.getAttributeManager() }

    override fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData) {
        EntityAttributeAPI.addEntityAttribute(entity, tempAttributeData.source, tempAttributeData.formatAttributes())
    }

    override fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData) {
        EntityAttributeAPI.addEntityAttribute(uuid.getLivingEntity()!!, tempAttributeData.source, tempAttributeData.formatAttributes())
    }

    override fun takeAttribute(entity: LivingEntity, source: String) {
        EntityAttributeAPI.removeEntityAttribute(entity, source)
    }

    override fun takeAttribute(uuid: UUID, source: String) {
        EntityAttributeAPI.removeEntityAttribute(uuid.getLivingEntity()!!, source)
    }

    override fun getAttributeValue(entity: LivingEntity, attribute: String, valueType: AttributeValueType): Double {
        val values = getData(entity)?.getAttributeValues(attribute) ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> random(values[0].cdouble, values[1].cdouble)
        }.cdouble
    }

    override fun getAttributeValue(uuid: UUID, attribute: String, valueType: AttributeValueType): Double {
        val values = getData(uuid)?.getAttributeValues(attribute) ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values[0]
            AttributeValueType.MAX -> values[1]
            AttributeValueType.RANDOM -> random(values[0].cdouble, values[1].cdouble)
        }.cdouble
    }

    override fun getData(entity: LivingEntity): AttributeData? {
        return attributeManager.getAttrData(entity)
    }

    override fun getData(uuid: UUID): AttributeData? {
        return getData(uuid.getLivingEntity()!!)
    }

}
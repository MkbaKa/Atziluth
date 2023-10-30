package me.mkbaka.atziluth.internal.module.attributes.datamanager

import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import java.util.*

interface AttributeDataManager<D> {

    /**
     * 增加属性
     * @param [entity] 实体
     * @param [tempAttributeData] 属性数据
     */
    fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData)

    /**
     * 增加属性
     * @param [uuid] UUID
     * @param [tempAttributeData] 属性数据
     */
    fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData)

    /**
     * 删除属性源对应的属性
     * @param [entity] 实体
     * @param [source] 属性源
     */
    fun takeAttribute(entity: LivingEntity, source: String)

    /**
     * 删除属性源对应的属性
     * @param [uuid] UUID
     * @param [source] 属性源
     */
    fun takeAttribute(uuid: UUID, source: String)

    /**
     * 获取属性值
     * @param [entity] 实体
     * @param [attribute] 属性名
     * @param [valueType] 数值类型
     */
    fun getAttributeValue(entity: LivingEntity, attribute: String, valueType: AttributeValueType = AttributeValueType.RANDOM): Double

    /**
     * 获取属性值
     * @param [uuid] UUID
     * @param [attribute] 属性名
     * @param [valueType] 数值类型
     */
    fun getAttributeValue(uuid: UUID, attribute: String, valueType: AttributeValueType = AttributeValueType.RANDOM): Double

    /**
     * 获取实体数据
     * @param [entity] 实体
     * @return D?
     */
    fun getData(entity: LivingEntity): D?

    /**
     * 获取数据
     * @param [uuid] UUID
     * @return D?
     */
    fun getData(uuid: UUID): D?

}
package me.mkbaka.atziluth.api

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import java.util.*

object AttributeAPI {

    private val dataManager by lazy { Atziluth.attributeHooker.attributeDataManager }

    /**
     * 获取属性值
     * @param [name] 属性名
     * @param [type] 数值类型
     * @return [Double]
     */
    fun UUID.getAttrValue(name: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
        return dataManager.getAttributeValue(this, name, type)
    }

    /**
     * 获取属性值
     * @param [name] 属性名
     * @param [type] 数值类型
     * @return [Double]
     */
    fun LivingEntity.getAttrValue(name: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
        return dataManager.getAttributeValue(this, name, type)
    }

    /**
     * 增加临时属性
     * @param [tempAttributeData] 临时属性数据
     */
    fun UUID.addAttribute(tempAttributeData: TempAttributeData) {
        dataManager.addAttribute(this, tempAttributeData)
    }

    /**
     * 增加临时属性
     * @param [tempAttributeData] 临时属性数据
     */
    fun LivingEntity.addAttribute(tempAttributeData: TempAttributeData) {
        dataManager.addAttribute(this, tempAttributeData)
    }

    /**
     * 删除已增加的临时属性
     * @param [source] 临时属性数据源
     */
    fun UUID.takeAttribute(source: String) {
        dataManager.takeAttribute(this, source)
    }
    
    /**
     * 删除已增加的临时属性
     * @param [source] 临时属性数据源
     */
    fun LivingEntity.takeAttribute(source: String) {
        dataManager.takeAttribute(this, source)
    }

}
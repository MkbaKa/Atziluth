package me.mkbaka.atziluth.api

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import java.util.*

object AttributeAPI {

    private val tempAttributeDataManager by lazy { Atziluth.tempAttributeDataManager }
    private val dataManager by lazy { Atziluth.attributeHooker.attributeDataManager }

    /**
     * 在最小与最大值中随机一个数值返回
     * 若不存在最大值/最大值与最小值相等
     * 则返回最小值
     * @param [name] 属性名
     * @param [type] 数值类型
     * @return [Double]
     */
    fun UUID.getAttrValue(name: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
        return dataManager.getAttributeValue(this, name, type)
    }

    /**
     * 在最小与最大值中随机一个数值返回
     * 若不存在最大值/最大值与最小值相等
     * 则返回最小值
     * @param [name] 属性名
     * @param [type] 数值类型
     * @return [Double]
     */
    fun LivingEntity.getAttrValue(name: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
        return dataManager.getAttributeValue(this, name, type)
    }


    /**
     * 获取属性最小值
     * @param [name] 属性名
     * @return [Double]
     */
    fun UUID.getMinValue(name: String): Double {
        return dataManager.getAttributeValue(this, name, AttributeValueType.MIN)
    }

    /**
     * 获取属性最小值
     * @param [name] 属性名
     * @return [Double]
     */
    fun LivingEntity.getMinValue(name: String): Double {
        return dataManager.getAttributeValue(this, name, AttributeValueType.MIN)
    }

    /**
     * 获取属性最大值
     * @param [name] 属性名
     * @return [Double]
     */
    fun UUID.getMaxValue(name: String): Double {
        return dataManager.getAttributeValue(this, name, AttributeValueType.MAX)
    }

    /**
     * 获取属性最大值
     * @param [name] 属性名
     * @return [Double]
     */
    fun LivingEntity.getMaxValue(name: String): Double {
        return dataManager.getAttributeValue(this, name, AttributeValueType.MAX)
    }

    /**
     * 增加临时属性
     * @param [tempAttributeData] 临时属性数据
     */
    fun UUID.addAttribute(tempAttributeData: TempAttributeData, stack: Boolean = false) {
        tempAttributeDataManager.addAttribute(this, tempAttributeData, stack)
    }

    /**
     * 增加临时属性
     * @param [tempAttributeData] 临时属性数据
     */
    fun LivingEntity.addAttribute(tempAttributeData: TempAttributeData, stack: Boolean = false) {
        tempAttributeDataManager.addAttribute(this, tempAttributeData, stack)
    }

    /**
     * 删除已增加的临时属性
     * @param [source] 临时属性数据源
     */
    fun UUID.takeAttribute(source: String) {
        tempAttributeDataManager.takeAttribute(this, source)
    }
    
    /**
     * 删除已增加的临时属性
     * @param [source] 临时属性数据源
     */
    fun LivingEntity.takeAttribute(source: String) {
        tempAttributeDataManager.takeAttribute(this, source)
    }

}
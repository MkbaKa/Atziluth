package me.mkbaka.atziluth.api

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.configuration.ConfigurationManager
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.abs

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
     * 获取物品上的属性值
     * @param [item] 物品
     * @param [attribute] 属性名
     * @param [valueType] 数值类型
     * @return [Double]
     */
    fun LivingEntity.getItemAttributeValue(
        item: ItemStack,
        attribute: String,
        valueType: AttributeValueType = AttributeValueType.RANDOM
    ): Double {
        return dataManager.getItemAttribute(this, item, attribute, valueType)
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

    /**
     * 清除实体身上的属性
     * @param [whitelist] 属性名白名单
     * @param [source] 属性数据源
     */
    fun LivingEntity.clearAttribute(whitelist: List<String>, source: String = UUID.randomUUID().toString()) {
        this.addAttribute(TempAttributeData.new(uniqueId, source, reverseAttributes(this, whitelist)))
    }

    /**
     * 清除实体身上的属性再执行callback
     * @param [whitelist] 属性名白名单
     * @param [callback] 回调函数
     */
    fun LivingEntity.clearAttribute(whitelist: List<String>, callback: () -> Unit) {
        val source = UUID.randomUUID().toString()
        clearAttribute(whitelist, source)
        callback()
        this.takeAttribute(source)
    }


    /**
     * 反转实体的属性值
     * 即每个属性对应的相反数
     * @param [entity] 实体
     * @param [whitelist] 属性名白名单
     * @return [MutableMap<String, DoubleArray>]
     */
    fun reverseAttributes(entity: LivingEntity, whitelist: List<String>): MutableMap<String, DoubleArray> {
        val map = hashMapOf<String, DoubleArray>()
        Atziluth.attributeHooker.getAllAttributes(whitelist).forEach { key ->
            if (key in whitelist || key in ConfigurationManager.clearAttributeWhiteList) return@forEach
            val minValue = entity.getAttrValue(key, AttributeValueType.MIN)
            if (minValue == 0.0) return@forEach

            val maxValue = entity.getAttrValue(key, AttributeValueType.MAX)

            when {
                maxValue > 0.0 -> map[key] = doubleArrayOf(-minValue, -maxValue)
                else -> map[key] = doubleArrayOf(abs(minValue), abs(maxValue))
            }
        }
        return map
    }

}
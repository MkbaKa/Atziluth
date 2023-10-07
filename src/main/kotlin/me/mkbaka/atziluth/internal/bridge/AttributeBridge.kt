package me.mkbaka.atziluth.internal.bridge

import me.mkbaka.atziluth.Atziluth.attributeBridge
import me.mkbaka.atziluth.Atziluth.attributeFactory
import me.mkbaka.atziluth.internal.bridge.tempattr.TempAttributeManager
import me.mkbaka.atziluth.internal.configuration.ConfigManager
import me.mkbaka.atziluth.internal.register.AttributeType
import org.bukkit.entity.LivingEntity
import java.util.*
import kotlin.math.abs

interface AttributeBridge {

    /**
     * 添加属性
     * @param [uuid] uuid
     * @param [source] 源
     * @param [attrs] 属性列表
     */
    fun addAttributes(uuid: UUID, source: String, attrs: List<String>)

    /**
     * 添加属性
     * @param [entity] 实体
     * @param [source] 源
     * @param [attrs] 属性列表
     */
    fun addAttributes(entity: LivingEntity, source: String, attrs: List<String>)

    /**
     * 删除属性源
     * @param [uuid] uuid
     * @param [source] 源
     */
    fun takeAttribute(uuid: UUID, source: String)

    /**
     * 删除属性源
     * @param [entity] 实体
     * @param [source] 源
     */
    fun takeAttribute(entity: LivingEntity, source: String)

    /**
     * 获取属性值
     * @param [uuid] uuid
     * @param [attrName] 属性名字
     * @param [type] 类型
     * @return [Double]
     */
    fun getAttrValue(uuid: UUID, attrName: String, type: AttributeValueType = AttributeValueType.RANDOM): Double

    /**
     * 获取属性值
     * @param [entity] 实体
     * @param [attrName] 属性名字
     * @param [type] 类型
     * @return [Double]
     */
    fun getAttrValue(
        entity: LivingEntity,
        attrName: String,
        type: AttributeValueType = AttributeValueType.RANDOM
    ): Double

    companion object {

        /**
         * 增加属性
         * @param [uuid] uuid
         * @param [source] 源
         * @param [attrs] 属性列表
         * @param [timeout] 持续时间 (tick)
         */
        fun addAttr(uuid: UUID, source: String, attrs: List<String>, timeout: Long = 0) {
            TempAttributeManager.addAttr(uuid, source, attrs, timeout)
        }

        /**
         * 增加属性
         * @param [entity] 实体
         * @param [source] 源
         * @param [attrs] 属性列表
         * @param [timeout] 持续时间 (tick)
         */
        fun addAttr(entity: LivingEntity, source: String, attrs: List<String>, timeout: Long = 0) {
            TempAttributeManager.addAttr(entity.uniqueId, source, attrs, timeout)
        }

        /**
         * 删除属性源
         * @param [uuid] uuid
         * @param [source] 源
         */
        fun takeAttr(uuid: UUID, source: String) {
            TempAttributeManager.takeAttr(uuid, source)
        }

        /**
         * 删除属性源
         * @param [entity] 实体
         * @param [source] 源
         */
        fun takeAttr(entity: LivingEntity, source: String) {
            TempAttributeManager.takeAttr(entity.uniqueId, source)
        }

        /**
         * 获取属性值
         * @param [uuid] uuid
         * @param [attr] attr
         * @param [type] 类型
         * @return [Double]
         */
        fun getAttrValue(uuid: UUID, attr: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
            return attributeBridge.getAttrValue(uuid, attr, type)
        }

        /**
         * 获取属性值
         * @param [entity] 实体
         * @param [attr] attr
         * @param [type] 类型
         * @return [Double]
         */
        fun getAttrValue(
            entity: LivingEntity,
            attr: String,
            type: AttributeValueType = AttributeValueType.RANDOM
        ): Double {
            return attributeBridge.getAttrValue(entity, attr, type)
        }

        /**
         * 注册空属性
         * @param [attrName] 属性名
         * @param [combatPower] 战斗力
         * @param [placeholder] 占位符
         */
        fun registerOtherAttribute(attrName: String, combatPower: Double, placeholder: String) {
            attributeFactory.buildAttribute(114514, attrName, placeholder, combatPower, AttributeType.OTHER).register()
        }

        /**
         * 清除玩家身上数值大于0的属性 执行callback后返回
         * @param [entity] 实体
         * @param [callback] 回调
         */
        fun clearAttributes(entity: LivingEntity, whiteListAttrs: List<String> = emptyList(), callback: (LivingEntity) -> Unit) {
            val source = UUID.randomUUID().toString()

            val list = arrayListOf<String>()
            attributeFactory.getAllAttributeNames().forEach {  attrName ->
                if (attrName in whiteListAttrs || attrName in ConfigManager.clearAttributeWhiteList) return@forEach
                val maxValue = getAttrValue(entity, attrName, AttributeValueType.MAX)
                if (maxValue == 0.0) return@forEach
                val minValue = getAttrValue(entity, attrName, AttributeValueType.MIN)

                list.add(
                    when {
                        maxValue > 0.0 -> "$attrName: -$minValue - -$maxValue"
                        else -> "$attrName: ${abs(minValue)} - ${abs(maxValue)}"
                    }
                )
            }

            addAttr(entity, source, list)
            callback(entity)
            takeAttr(entity, source)
        }

    }

}

enum class AttributeValueType {
    MIN, MAX, RANDOM;

    companion object {

        fun of(str: String): AttributeValueType? {
            return values().firstOrNull { str.equals(it.name, ignoreCase = true) }
        }

    }
}
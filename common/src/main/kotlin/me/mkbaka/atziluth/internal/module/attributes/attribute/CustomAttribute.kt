package me.mkbaka.atziluth.internal.module.attributes.attribute

import me.mkbaka.atziluth.internal.module.attributes.attribute.impl.CustomAttributeImpl
import me.mkbaka.atziluth.internal.module.fightdata.FightData
import org.bukkit.entity.LivingEntity

/**
 * 代表一个由Atziluth创建的属性
 */
interface CustomAttribute {

    /**
     * 属性名
     */
    val attributeName: String

    /**
     * 属性类型
     */
    val attributeType: CustomAttributeType

    /**
     * 触发阶段
     */
    var isBefore: Boolean

    /**
     * 属性优先级
     */
    var priority: Int

    /**
     * 变量
     */
    val placeholder: String

    /**
     * 战斗力
     */
    var combatPower: Double

    /**
     * 仅于 runtime 类型生效
     * 触发间隔
     */
    var period: Int

    /**
     * 是否跳过属性值过滤直接触发
     * 为true时属性值小于等于0也会触发
     */
    var skipFilter: Boolean

    /**
     * 属性加载时触发的函数
     */
    var onLoad: () -> Unit

    /**
     * attack defense 类触发时的函数
     */
    var callback: (FightData, Map<String, Any>) -> Unit

    /**
     * runtime update 类触发的函数
     */
    var run: (LivingEntity) -> Unit

    companion object {


        /**
         * 构造一个基础的 CustomAttribute 对象
         * @param [name] 属性名
         * @param [type] 属性类型
         * @param [placeholder] 变量
         * @param [callback] 构造回调
         * @return [CustomAttribute]
         */
        fun buildAttribute(name: String, type: CustomAttributeType, placeholder: String, callback: CustomAttribute.() -> Unit): CustomAttribute {
            return CustomAttributeImpl(name, type, placeholder).also(callback)
        }

    }

}
package me.mkbaka.atziluth.internal.register

import me.mkbaka.atziluth.Atziluth.attributeFactory

abstract class AttributeFactory<T : AbstractCustomAttribute<*>> {

    /**
     * 重载
     */
    abstract fun reload()

    /**
     * 注册属性
     * @param [customAttribute] 自定义属性
     */
    abstract fun registerAttribute(customAttribute: T)

    /**
     * 注册所有属性后的回调
     * 为了某些特殊需求
     */
    open fun registeredCallback() {}

    /** 包名 */
    protected val packageName: String by lazy { this::class.java.`package`.name }

    /**
     * 构造属性
     * @param [name] 名字
     * @param [placeholder] 占位符
     * @param [type] 类型
     * @return [T]
     */
    open fun buildAttribute(
        name: String,
        placeholder: String,
        type: AttributeType
    ): T {
        return error("Not yet implemented")
    }

    /**
     * 构造属性
     * @param [priority] 优先级
     * @param [name] 名字
     * @param [placeholder] 占位符
     * @param [combatPower] 战斗力
     * @param [type] 类型
     * @return [T]
     */
    abstract fun buildAttribute(
        priority: Int,
        name: String,
        placeholder: String,
        combatPower: Double,
        type: AttributeType
    ): T

    /**
     * 是否为已注册的属性
     * @param [name] 名字
     * @return [Boolean]
     */
    abstract fun isAttribute(name: String): Boolean

    abstract fun getAllAttributeNames(): Collection<String>

    companion object {

        fun registerAttributes(attrs: Collection<AbstractCustomAttribute<*>>) {
            attrs.forEach { attr ->
                attr.register()
                attr.onLoad?.let { it(attr) }
            }
            attributeFactory.registeredCallback()
        }

        /**
         * 构造属性
         * @param [priority] 优先级
         * @param [name] 名字
         * @param [placeholder] 占位符
         * @param [combatPower] 战斗力
         * @param [type] 类型
         * @return [AbstractCustomAttribute<*>]
         */
        fun buildAttribute(
            priority: Int,
            name: String,
            placeholder: String,
            combatPower: Double,
            type: AttributeType
        ): AbstractCustomAttribute<*> {
            return attributeFactory.buildAttribute(priority, name, placeholder, combatPower, type)
        }

        /**
         * 构造属性
         * @param [name] 名字
         * @param [placeholder] 占位符
         * @param [type] 类型
         * @return [AbstractCustomAttribute<*>]
         */
        fun buildAttribute(
            name: String,
            placeholder: String,
            type: AttributeType
        ): AbstractCustomAttribute<*> {
            return attributeFactory.buildAttribute(name, placeholder, type)
        }

        /**
         * 是否为已注册的属性
         * @param [name] 名字
         * @return [Boolean]
         */
        fun isAttribute(name: String): Boolean {
            return attributeFactory.isAttribute(name)
        }

    }

}
package me.mkbaka.atziluth.internal.register

import me.mkbaka.atziluth.Atziluth.attributeFactory

abstract class AttributeFactory<T : AbstractCustomAttribute<*, *>> {

    abstract fun reload()

    abstract fun registerAttribute(customAttribute: T)

    open fun registeredCallback() {}

    protected val packageName: String by lazy { this::class.java.`package`.name }

    open fun unRegisterAttribute(customAttribute: T) {
        error("Unimplemented method")
    }

    open fun buildAttribute(
        name: String,
        placeholder: String,
        type: AttributeType
    ): T {
        return error("Not yet implemented")
    }

    abstract fun buildAttribute(
        priority: Int,
        name: String,
        placeholder: String,
        combatPower: Double,
        type: AttributeType
    ): T

    abstract fun isAttribute(name: String): Boolean

    companion object {

        fun buildAttribute(
            priority: Int,
            name: String,
            placeholder: String,
            combatPower: Double,
            type: AttributeType
        ): AbstractCustomAttribute<*, *> {
            return attributeFactory.buildAttribute(priority, name, placeholder, combatPower, type)
        }

        fun buildAttribute(
            name: String,
            placeholder: String,
            type: AttributeType
        ): AbstractCustomAttribute<*, *> {
            return attributeFactory.buildAttribute(name, placeholder, type)
        }

        fun isAttribute(name: String): Boolean {
            return attributeFactory.isAttribute(name)
        }

        fun onReload() {
            attributeFactory.reload()
        }

    }

}
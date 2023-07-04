package me.mkbaka.atziluth.internal.register

import org.bukkit.entity.LivingEntity

/**
 * 自定义属性
 * @param T 属性接口类
 */
abstract class AbstractCustomAttribute<T> {

    open val attrPriority: Int = 0
    open val combatPower: Double = 0.0

    open var skipFilter: Boolean = false
    open var period: Long = 5L

    abstract val inst: T

    abstract val name: String
    abstract val placeholder: String

    abstract val type: AttributeType

    var onLoad: ((AbstractCustomAttribute<*>) -> Unit)? = null
    var callback: ((AbstractCustomAttribute<*>, LivingEntity, LivingEntity) -> Boolean)? = null
    var runtimeCallback: ((Any, LivingEntity) -> Boolean)? = null

    fun onLoad(callback: (AbstractCustomAttribute<*>) -> Unit) {
        this.onLoad = callback
    }

    fun callback(block: (AbstractCustomAttribute<*>, LivingEntity, LivingEntity) -> Boolean) {
        this.callback = block
    }

    fun run(block: (Any, LivingEntity) -> Boolean) {
        this.runtimeCallback = block
    }

    abstract fun register()

}

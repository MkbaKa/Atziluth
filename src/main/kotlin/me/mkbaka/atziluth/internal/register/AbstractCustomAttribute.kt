package me.mkbaka.atziluth.internal.register

import org.bukkit.entity.LivingEntity

/**
 * 自定义属性
 * @param I 属性接口类
 * @param T 属性类型
 */
abstract class AbstractCustomAttribute<I, T> {

    open val attrPriority: Int = 0
    open val combatPower: Double = 0.0

    abstract val name: String
    abstract val placeholder: String

    abstract val type: AttributeType

    var onLoad: ((I) -> Unit)? = null
    var callback: ((I, LivingEntity, LivingEntity) -> Boolean)? = null

    open val period: Long = 0

    fun onLoad(callback: (I) -> Unit) {
        this.onLoad = callback
    }

    fun callback(block: (I, LivingEntity, LivingEntity) -> Boolean) {
        this.callback = block
    }

    abstract fun build(): I

    abstract fun getType(): T

    abstract fun register()

}

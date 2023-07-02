package me.mkbaka.atziluth.internal.listener

import me.mkbaka.atziluth.internal.utils.ClassUtil.toBukkitEvent
import org.bukkit.event.Event
import taboolib.common.platform.event.EventPriority

/**
 * 一个简单的监听器对象
 * @param [source] 源
 */
class SimpleListener(val source: String) {

    lateinit var eventClass: Class<out Event>
    lateinit var callback: (Event) -> Unit
    var priority = EventPriority.LOWEST
    var ignoreCancelled = false

    /**
     * 绑定到事件
     * @param [clazz] clazz
     * @return [SimpleListener]
     */
    fun bindToEvent(clazz: String): SimpleListener {
        this.eventClass = clazz.toBukkitEvent()
        return this
    }

    /**
     * 绑定到类
     * @param [clazz] clazz
     * @return [SimpleListener]
     */
    fun bindToClass(clazz: Class<out Event>): SimpleListener {
        this.eventClass = clazz
        return this
    }

    /**
     * 设置优先级
     * @param [priority] 优先级
     * @return [SimpleListener]
     */
    fun setPriority(priority: EventPriority): SimpleListener {
        this.priority = priority
        return this
    }

    /**
     * 设置忽略取消
     * @param [ignoreCancelled] 忽略取消
     * @return [SimpleListener]
     */
    fun setIgnoreCancelled(ignoreCancelled: Boolean): SimpleListener {
        this.ignoreCancelled = ignoreCancelled
        return this
    }

    /**
     * 设置执行器
     * @param [func] 函数
     * @return [SimpleListener]
     */
    fun setExecutor(func: (Event) -> Unit): SimpleListener {
        this.callback = func
        return this
    }

    /**
     * 注册
     */
    fun register() {
        ListenerManager.registerListener(this)
    }

    /**
     * 注销
     */
    fun unRegister() {
        ListenerManager.unRegisterListener(this)
    }

}
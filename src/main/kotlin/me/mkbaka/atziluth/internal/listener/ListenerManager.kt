package me.mkbaka.atziluth.internal.listener

import me.mkbaka.atziluth.internal.utils.callSync
import org.bukkit.event.Event
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import java.util.concurrent.ConcurrentHashMap

object ListenerManager {

    private val listeners = ConcurrentHashMap<String, ProxyListener>()

    /**
     * 注册监听器
     * @param [listener] 监听器
     */
    fun registerListener(
        listener: SimpleListener
    ) {
        registerListener(
            listener.source,
            listener.eventClass,
            listener.priority,
            listener.ignoreCancelled,
            listener.callback
        )
    }

    /**
     * 注册监听器
     * @param [source] 源
     * @param [clazz] 事件类
     * @param [priority] 优先级
     * @param [ignore] 是否忽略取消
     * @param [callback] 回调函数
     */
    fun registerListener(
        source: String,
        clazz: Class<out Event>,
        priority: EventPriority = EventPriority.LOWEST,
        ignore: Boolean = false,
        callback: (Event) -> Unit
    ) {
        callSync {
            if (listeners.containsKey(source)) unregisterListener(listeners[source]!!)
            listeners[source] = registerBukkitListener(clazz, priority, ignore) { callback(it) }
        }
    }

    /**
     * 注销监听器
     * @param [listener] 监听器
     */
    fun unRegisterListener(listener: SimpleListener) {
        unRegisterListener(listener.source)
    }

    /**
     * 注销监听器
     * @param [source] 源
     */
    fun unRegisterListener(source: String) {
        callSync {
            if (listeners.containsKey(source)) {
                unregisterListener(listeners[source]!!)
                listeners.remove(source)
            }
        }
    }

    /**
     * 注销所有监听器
     */
    fun unRegisterAllListener() {
        callSync {
            listeners.forEach {
                unregisterListener(it.value)
            }
        }
    }

}
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

    fun unRegisterListener(listener: SimpleListener) {
        callSync {
            if (listeners.containsKey(listener.source)) unregisterListener(listeners[listener.source]!!)
        }
    }

    fun unRegisterListener(source: String) {
        callSync {
            if (listeners.containsKey(source)) unregisterListener(listeners[source]!!)
        }
    }

    fun unRegisterAllListener() {
        callSync {
            listeners.forEach {
                unregisterListener(it.value)
            }
        }
    }

}
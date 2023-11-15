package me.mkbaka.atziluth.utils.scriptutil.listener

import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
import me.mkbaka.atziluth.utils.EventUtil
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import taboolib.common.platform.event.SubscribeEvent
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

object ListenerManager {

    private val listeners = ConcurrentHashMap<String, org.bukkit.event.Listener>()

    fun register(listener: Listener) {
        listeners.compute(listener.source) { _, oldValue ->
            oldValue?.let { unregister(listener) }
            object : org.bukkit.event.Listener {}.apply {
                Bukkit.getPluginManager().registerEvent(
                    listener.eventClass, this, listener.eventPriority,
                    { _, event -> if (listener.eventClass.isAssignableFrom(event::class.java)) listener.exec(event) },
                    listener.owner, listener.ignore
                )
            }
        }
    }

    fun unregister(listener: Listener) {
        unregister(listener.source)
    }

    fun unregister(source: String) {
        val listener = listeners[source] ?: return
        HandlerList.unregisterAll(listener)
        listeners.remove(source)
    }

    fun registerAllListener(filter: (Class<out Event>) -> Boolean, func: (Event) -> Unit) {
        EventUtil.getAllEventClasses().forEach { clazz ->
            if (!Modifier.isAbstract(clazz.modifiers) && !clazz.isInterface && filter(clazz)) {
                Listener("Atziluth-${clazz.simpleName}").also {
                    it.eventClass = clazz
                    it.exec = func
                }.register()
            }
        }
    }

    @SubscribeEvent
    fun reload(e: AtziluthReloadEvent) {
        if (e.status == ReloadStatus.PRE) {
            listeners.forEach { (source, _) ->
                unregister(source)
            }
            listeners.clear()
        }
    }

}
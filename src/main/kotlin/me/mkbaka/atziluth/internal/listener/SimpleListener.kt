package me.mkbaka.atziluth.internal.listener

import me.mkbaka.atziluth.internal.utils.ClassUtil.toBukkitEvent
import org.bukkit.event.Event
import taboolib.common.platform.event.EventPriority

class SimpleListener(val source: String) {

    lateinit var eventClass: Class<out Event>
    lateinit var callback: (Event) -> Unit
    var priority = EventPriority.LOWEST
    var ignoreCancelled = false

    fun bindToEvent(clazz: String): SimpleListener {
        this.eventClass = clazz.toBukkitEvent()
        return this
    }

    fun bindToClass(clazz: Class<out Event>): SimpleListener {
        this.eventClass = clazz
        return this
    }

    fun setPriority(priority: EventPriority): SimpleListener {
        this.priority = priority
        return this
    }

    fun setIgnoreCancelled(ignoreCancelled: Boolean): SimpleListener {
        this.ignoreCancelled = ignoreCancelled
        return this
    }

    fun setExecutor(func: (Event) -> Unit): SimpleListener {
        this.callback = func
        return this
    }

    fun register() {
        ListenerManager.registerListener(this)
    }

    fun unRegister() {
        ListenerManager.unRegisterListener(this)
    }

}
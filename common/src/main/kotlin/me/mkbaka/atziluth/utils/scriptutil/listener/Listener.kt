package me.mkbaka.atziluth.utils.scriptutil.listener

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.utils.ClassUtil.toBukkitEvent
import me.mkbaka.atziluth.utils.enumOf
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class Listener(
    val source: String
) {

    lateinit var eventClass: Class<out Event>

    var eventPriority = EventPriority.LOWEST

    var ignore = false

    var exec: (Event) -> Unit = { _, -> }

    var owner: Plugin = Atziluth.plugin

    fun bindToEvent(className: String): Listener {
        this.eventClass = className.toBukkitEvent()
        return this
    }

    fun bindToEvent(clazz: Class<Event>): Listener {
        this.eventClass = clazz
        return this
    }

    fun setPriority(eventPriority: EventPriority): Listener {
        this.eventPriority = eventPriority
        return this
    }

    fun setPriority(priority: String): Listener {
        this.eventPriority = enumOf<EventPriority>(priority) ?: error("错误的事件优先级 $priority")
        return this
    }

    fun setIgnoreCancelled(ignore: Boolean): Listener {
        this.ignore = ignore
        return this
    }

    fun setPlugin(plugin: Plugin): Listener {
        this.owner = plugin
        return this
    }

    fun setPlugin(name: String): Listener {
        this.owner = Bukkit.getPluginManager().getPlugin(name) ?: error("未获取到名为 $name 的插件")
        return this
    }

    fun setExecutor(exec: (Event) -> Unit): Listener {
        this.exec = exec
        return this
    }

    fun register() {
        ListenerManager.register(this)
        console().sendLang("register-listener", prefix, source, eventClass.name)
    }

    fun unregister() {
        ListenerManager.unregister(this)
        console().sendLang("unregister-listener", prefix, source, eventClass.name)
    }

}
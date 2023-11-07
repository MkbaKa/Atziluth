package me.mkbaka.atziluth.utils.scriptutil.command

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
import me.mkbaka.atziluth.utils.SchedulerUtil.callSync
import me.mkbaka.atziluth.utils.obc.OBCUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.getProperty
import java.util.concurrent.ConcurrentHashMap

object CommandUtil {

    private val commandMap by lazy {
        Bukkit.getPluginManager().getProperty<SimpleCommandMap>("commandMap")!!
    }

    private val knownCommands by lazy {
        commandMap.getProperty<MutableMap<String, Command>>("knownCommands")!!
    }

    private val constructor by lazy {
        PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java).also {
            it.isAccessible = true
        }
    }

    val registeredCommands = ConcurrentHashMap<String, me.mkbaka.atziluth.utils.scriptutil.command.Command>()

    fun newCommand(name: String): PluginCommand {
        return newCommand(name, Atziluth.plugin)
    }

    fun newCommand(name: String, plugin: Plugin): PluginCommand {
        return constructor.newInstance(name, plugin)
    }

    fun unregisterCommand(name: String) {
        callSync {
            knownCommands.remove(name)
            OBCUtil.syncCommands()
        }
        registeredCommands.remove(name)
    }

    fun register(command: me.mkbaka.atziluth.utils.scriptutil.command.Command) {
        registeredCommands["${command.namespace}:${command.name}"]?.unregister()
        callSync {
            commandMap.register(command.namespace, command.pluginCommand)
            OBCUtil.syncCommands()
        }
        registeredCommands["${command.namespace}:${command.name}"] = command
    }

    @SubscribeEvent
    fun reload(e: AtziluthReloadEvent) {
        if (e.status == ReloadStatus.PRE) {
            registeredCommands.forEach { (_, command) ->
                command.unregister()
            }
            registeredCommands.clear()
        }
    }

}
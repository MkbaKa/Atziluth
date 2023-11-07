package me.mkbaka.atziluth.utils.scriptutil.command

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.Atziluth.prefix
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class Command {

    constructor(name: String) {
        this.name = name
        this.ownerPlugin = Atziluth.plugin
        this.pluginCommand = CommandUtil.newCommand(name, ownerPlugin)
        this.namespace = "AtziluthCommand:$name"
    }

    constructor(name: String, plugin: Plugin) {
        this.name = name
        this.ownerPlugin = plugin
        this.pluginCommand = CommandUtil.newCommand(name, plugin)
        this.namespace = "AtziluthCommand:$name"
    }

    val name: String

    val ownerPlugin: Plugin

    val pluginCommand: PluginCommand

    var namespace: String

    fun setNameSpace(name: String): Command {
        this.namespace = name
        return this
    }

    fun setExecutor(exec: CommandExecutor): Command {
        pluginCommand.setExecutor(exec)
        return this
    }

    fun setTabCompleter(tabCompleter: TabCompleter): Command {
        pluginCommand.tabCompleter = tabCompleter
        return this
    }

    fun setPermission(perm: String): Command {
        pluginCommand.permission = perm
        return this
    }

    fun setPermissionMessage(msg: String): Command {
        pluginCommand.permissionMessage = msg
        return this
    }

    fun setLabel(label: String): Command {
        pluginCommand.label = label
        return this
    }

    fun setAliases(aliases: List<String>): Command {
        pluginCommand.aliases = aliases
        return this
    }

    fun setUsage(usage: String): Command {
        pluginCommand.usage = usage
        return this
    }

    fun register() {
        CommandUtil.register(this)
        console().sendLang("register-command", prefix, name)
    }

    fun unregister() {
        CommandUtil.unregisterCommand(name)
        CommandUtil.unregisterCommand("$namespace:$name")
        pluginCommand.aliases.forEach { alias ->
            CommandUtil.unregisterCommand(alias)
            CommandUtil.unregisterCommand("$namespace:$alias")
        }
        console().sendLang("unregister-command", prefix, name)
    }

}
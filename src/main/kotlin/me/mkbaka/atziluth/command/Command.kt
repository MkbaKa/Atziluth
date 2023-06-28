package me.mkbaka.atziluth.command

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.internal.configuration.ConfigManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

@CommandHeader("atziluth", aliases = ["atz", "atl"], permissionDefault = PermissionDefault.OP)
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            ConfigManager.reload()
            sender.sendLang("reload", prefix)
        }
    }
}
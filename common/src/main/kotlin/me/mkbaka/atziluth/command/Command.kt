package me.mkbaka.atziluth.command

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

@CommandHeader("atziluth", aliases = ["atl", "atz"], permissionDefault = PermissionDefault.OP)
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, context, argument ->
            AbstractConfigComponent.reloadAll()
            sender.sendLang("reload", prefix)
        }
    }

}
package me.mkbaka.atziluth.command

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
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
            AtziluthReloadEvent(ReloadStatus.PRE).call()
            AbstractConfigComponent.reloadAll()
            AtziluthReloadEvent(ReloadStatus.POST).call()
            sender.sendLang("reload", prefix)
        }
    }

}
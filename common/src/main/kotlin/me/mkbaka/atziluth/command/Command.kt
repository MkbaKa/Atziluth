package me.mkbaka.atziluth.command

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
import me.mkbaka.atziluth.command.expansion.sendCommandHelper
import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.configuration.impl.ScriptLibsComponent
import me.mkbaka.atziluth.internal.configuration.impl.ScriptsComponent
import me.mkbaka.atziluth.utils.SchedulerUtil.callAsync
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common5.eqic
import taboolib.module.lang.sendLang

@CommandHeader("atziluth", aliases = ["atl", "atz"], permissionDefault = PermissionDefault.OP)
object Command {

    @CommandBody
    val main = mainCommand {
        sendCommandHelper()
    }

    @CommandBody
    val script = ScriptCommand

    @CommandBody
    val reload = subCommand {
        dynamic("module") {
            suggestionUncheck<ProxyCommandSender> { sender, context ->
                ReloadType.values().map { it.name }
            }

            execute<ProxyCommandSender> { sender, context, argument ->
                val type = ReloadType.of(context["module"]) ?: ReloadType.ALL
                callReload(sender, type)
            }
        }
        execute<ProxyCommandSender> { sender, context, argument ->
            callReload(sender, ReloadType.ALL)
        }
    }

    private fun callReload(sender: ProxyCommandSender, type: ReloadType) {
        callAsync {
            when (type) {
                ReloadType.ATTRIBUTE -> {
                    // 清理编译缓存
                    AbstractConfigComponent.reload(1)
                    // 重载属性
                    AttributeManagerComponent.reload()
                    sender.sendLang("reload-attribute", prefix)
                }

                ReloadType.SCRIPT -> {
                    // 清理编译缓存
                    AbstractConfigComponent.reload(1)
                    // 注销脚本注册的各种扩展
                    AtziluthReloadEvent(ReloadStatus.PRE).call()
                    // 重载脚本模块
                    ScriptLibsComponent.reload()
                    ScriptsComponent.reload()
                    sender.sendLang("reload-script", prefix)
                }

                else -> {
                    AtziluthReloadEvent(ReloadStatus.PRE).call()
                    AbstractConfigComponent.reloadAll()
                    AtziluthReloadEvent(ReloadStatus.POST).call()
                    sender.sendLang("reload-all", prefix)
                }
            }
        }
    }

    enum class ReloadType(val aliases: Array<String> = emptyArray()) {
        ATTRIBUTE(arrayOf("attr", "attrs", "attribute")),
        SCRIPT(arrayOf("sc", "script")),
        ALL(arrayOf("all"));

        companion object {

            fun of(str: String): ReloadType? {
                return ReloadType.values()
                    .firstOrNull { type -> type.name.eqic(str) || type.aliases.any { it.eqic(str) } }
            }

        }

    }

}
package me.mkbaka.atziluth.command

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.internal.configuration.impl.ScriptsComponent
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandContext
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object ScriptCommand {

    @CommandBody
    val eval = subCommand {
        dynamic("script") {
            execute<ProxyCommandSender> { sender, context, argument ->
                val script = AbstractScriptFactory.compile(context["script"])
                    ?: return@execute sender.sendLang("undefined-script-type", prefix)
                sender.sendLang(
                    "script-executed",
                    prefix,
                    context["script"],
                    script.evalScript(
                        hashMapOf(
                            "sender" to sender,
                            "context" to context
                        )
                    ) ?: "null"
                )

            }
        }
    }

    @CommandBody
    val invoke = subCommand {
        dynamic("path") {
            dynamic("function") {
                execute<ProxyCommandSender> { sender, context, argument ->
                    invokeFunction(sender, context)
                }
            }
        }
    }

    private fun invokeFunction(sender: ProxyCommandSender, context: CommandContext<*>) {
        val script = ScriptsComponent.scripts[context["path"]] ?: return sender.sendLang(
            "undefined-script",
            prefix,
            context["path"]
        )
        if (!script.isFunction(context["function"])) return sender.sendLang(
            "undefined-function",
            prefix,
            context["function"]
        )

        sender.sendLang(
            "script-executed",
            prefix,
            context["function"],
            script.invokeFunction(context["function"], hashMapOf("sender" to sender, "context" to context)) ?: "null"
        )
    }
}
package me.mkbaka.atziluth.internal.utils

import org.bukkit.command.CommandSender
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether

object KetherUtil {

    fun String.eval(sender: CommandSender, args: Map<String, Any> = emptyMap()): Any? {
        return runKether {
            KetherShell.eval(this, ScriptOptions.new {
                sender(sender)
                vars(args)
            })
        }
    }

}
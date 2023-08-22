package me.mkbaka.atziluth.internal.utils

import me.mkbaka.atziluth.Atziluth.namespaces
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether

object KetherUtil {

    fun String.eval(sender: CommandSender?, args: Map<String, Any> = emptyMap()): Any? {
        return runKether(null) {
            KetherShell.eval(this, ScriptOptions.new {
                namespace(namespaces)
                sender(sender ?: Bukkit.getConsoleSender())
                vars(args)
            }).get()
        }
    }

}
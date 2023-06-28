package me.mkbaka.atziluth.internal.utils

import org.bukkit.plugin.Plugin
import taboolib.common5.cint
import taboolib.module.chat.colored
import taboolib.module.chat.uncolored

object Util {

    val Plugin.version: Int
        get() = description.version[0].cint

    fun colored(str: String): String {
        return str.colored()
    }

    fun uncolored(str: String): String {
        return str.uncolored()
    }

}
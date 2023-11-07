package me.mkbaka.atziluth.utils

import taboolib.module.chat.colored
import taboolib.module.chat.uncolored

object StringUtil {

    fun colored(str: String): String {
        return str.colored()
    }

    fun uncolor(str: String): String {
        return str.uncolored()
    }

    fun colored(list: List<String>): List<String> {
        return list.colored()
    }

    fun uncolor(list: List<String>): List<String> {
        return list.uncolored()
    }

}
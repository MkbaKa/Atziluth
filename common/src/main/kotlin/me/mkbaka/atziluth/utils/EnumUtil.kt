package me.mkbaka.atziluth.utils

import taboolib.common5.eqic

inline fun <reified T : Enum<T>> enumOf(str: String): T? {
    return enumValues<T>().firstOrNull { it.name.eqic(str) }
}
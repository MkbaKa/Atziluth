package me.mkbaka.atziluth.utils

object ClassUtil {

    val Class<*>.instance: Any?
        get() = kotlin.runCatching {
            getDeclaredField("INSTANCE").get(null) ?: getField("instance").get(null) ?: getConstructor().newInstance()
        }.getOrNull()

}
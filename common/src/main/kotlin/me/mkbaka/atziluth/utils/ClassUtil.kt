package me.mkbaka.atziluth.utils

import taboolib.library.reflex.Reflex.Companion.invokeMethod
import java.net.JarURLConnection
import java.net.URLClassLoader

object ClassUtil {

    val Class<*>.instance: Any?
        get() = kotlin.runCatching {
            getDeclaredField("INSTANCE").get(null) ?: getField("instance").get(null) ?: getConstructor().newInstance()
        }.getOrNull()

    fun loadResourceJar(path: String) {
        val resourceJar = this::class.java.classLoader.getResource(path)!!

        val url = try {
            resourceJar.toURI()
        } catch (ex: IllegalArgumentException) {
            (resourceJar.openConnection() as JarURLConnection).jarFileURL.toURI()
        }.toURL()

        val classLoader = (Thread.currentThread().contextClassLoader as URLClassLoader)
        classLoader.invokeMethod<Unit>("addURL", url)
    }

}
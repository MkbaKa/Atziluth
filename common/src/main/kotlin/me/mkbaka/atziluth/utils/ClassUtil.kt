package me.mkbaka.atziluth.utils

import org.bukkit.event.Event
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.platform.BukkitEvent
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.JarURLConnection
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap

object ClassUtil {

    private val classes = ConcurrentHashMap<String, Class<*>>()

    val Class<*>.instance: Any?
        get() = kotlin.runCatching {getDeclaredField("INSTANCE").get(null) }.getOrNull()
                ?: kotlin.runCatching { getField("instance").get(null) }.getOrNull()
                ?: kotlin.runCatching { getConstructor().newInstance() }.getOrNull()

    /**
     * 根据全限定类名获取一个Class对象
     * @return [Class<*>]
     */
    @Suppress("UNCHECKED_CAST")
    fun String.getClass(): Class<*> {
        return try {
            classes.computeIfAbsent(this) {
                Class.forName(this)
            }
        } catch (e: ClassNotFoundException) {
            error("未找到名为 $this 的类, 请检查拼写是否错误")
        }
    }

    /**
     * 获取一个Bukkit事件类
     * @return [Class<out Event>]
     */
    @Suppress("UNCHECKED_CAST")
    fun String.toBukkitEvent(): Class<out Event> {
        val clazz = getClass()
        return if (clazz.isBukkitEvent()) clazz as Class<out Event> else error("类 $this 不是 Event 的子类或为抽象类!")
    }

    /**
     * 是否为Bukkit事件类
     * @return [Boolean]
     */
    fun Class<*>.isBukkitEvent(): Boolean {
        return (Event::class.java.isAssignableFrom(this) || BukkitEvent::class.java.isAssignableFrom(this))
            && this.simpleName != "Event" && !Modifier.isAbstract(this.modifiers)
    }

    /**
     * @link https://github.com/Glom-c/Pouvoir
     * @author Glom-c
     */
    private val getStaticClass: Method by lazy(LazyThreadSafetyMode.NONE) {
        try {
            Class.forName("!jdk.internal.dynalink.beans.StaticClass".substring(1))
        } catch (throwable: Throwable) {
            Class.forName("jdk.dynalink.beans.StaticClass")
        }.getMethod("forClass", Class::class.java)
    }

    fun Class<*>.static(): Any {
        return getStaticClass.invoke(null, this)!!
    }

    fun staticClass(className: String): Any {
        return className.getClass().static()
    }

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
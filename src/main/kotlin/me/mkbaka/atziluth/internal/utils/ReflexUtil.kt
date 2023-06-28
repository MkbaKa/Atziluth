package me.mkbaka.atziluth.internal.utils

import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty

object ReflexUtil {

    fun setProperty(obj: Any, path: String, value: Any?) {
        obj.setProperty(path, value)
    }

    fun setProperty(obj: Any, path: String, value: Any?, isStatic: Boolean) {
        obj.setProperty(path, value, isStatic)
    }

    fun setProperty(obj: Any, path: String, value: Any?, isStatic: Boolean, findToParent: Boolean) {
        obj.setProperty(path, value, isStatic, findToParent)
    }

    fun setProperty(obj: Any, path: String, value: Any?, isStatic: Boolean, findToParent: Boolean, remap: Boolean) {
        obj.setProperty(path, value, isStatic, findToParent, remap)
    }

    fun getProperty(obj: Any, path: String): Any? {
        return obj.getProperty(path)
    }

    fun getProperty(obj: Any, path: String, isStatic: Boolean): Any? {
        return obj.getProperty(path, isStatic)
    }

    fun getProperty(obj: Any, path: String, isStatic: Boolean, findToParent: Boolean): Any? {
        return obj.getProperty(path, isStatic, findToParent)
    }

    fun getProperty(obj: Any, path: String, isStatic: Boolean, findToParent: Boolean, remap: Boolean): Any? {
        return obj.getProperty(path, isStatic, findToParent, remap)
    }

    fun <T> invokeMethod(obj: Any, method: String): T? {
        return obj.invokeMethod(method)
    }

    fun <T> invokeMethod(obj: Any, method: String, vararg args: Any): T? {
        return obj.invokeMethod(method, args)
    }

    fun <T> invokeMethod(obj: Any, method: String, vararg args: Any, isStatic: Boolean): T? {
        return obj.invokeMethod(method, args, isStatic)
    }

    fun <T> invokeMethod(obj: Any, method: String, vararg args: Any, isStatic: Boolean, findToParent: Boolean): T? {
        return obj.invokeMethod(method, args, isStatic, findToParent)
    }

    fun <T> invokeMethod(
        obj: Any,
        method: String,
        vararg args: Any,
        isStatic: Boolean,
        findToParent: Boolean,
        remap: Boolean
    ): T? {
        return obj.invokeMethod(method, args, isStatic, findToParent, remap)
    }

}
package me.mkbaka.atziluth.internal.module.script

interface Script {

    fun <T> getTopLevel(name: String): T?

    fun <T> getTopLevel(name: String, def: T): T

    fun evalScript(args: Map<String, Any>): Any?

    fun invokeFunction(func: String, topLevels: Map<String, Any> = hashMapOf(), vararg args: Any): Any?

    fun isFunction(func: String): Boolean

}
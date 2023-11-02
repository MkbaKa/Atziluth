package me.mkbaka.atziluth.internal.module.hook.mythicmobs.script

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class ProxyScriptCondition(val names: List<String>) {

    // 给 js 用 不需要考虑类型
    var castEntityCallback: (Any, Map<String, Any>) -> Boolean = { _, _ -> true }

    var castLocationCallback: (Any, Map<String, Any>) -> Boolean = { _, _ -> true }

    // 方便js更改
    fun checkEntity(callback: (Any, Map<String, Any>) -> Boolean): ProxyScriptCondition {
        this.castEntityCallback = callback
        return this
    }

    // 方便js更改
    fun checkLocation(callback: (Any, Map<String, Any>) -> Boolean): ProxyScriptCondition {
        this.castLocationCallback = callback
        return this
    }

    fun register() {
        names.forEach { name ->
            AbstractMythicMobsHooker.scriptConditions.computeIfAbsent(name) { this }
        }
        console().sendLang("register-condition", Atziluth.prefix, names[0])

    }

}
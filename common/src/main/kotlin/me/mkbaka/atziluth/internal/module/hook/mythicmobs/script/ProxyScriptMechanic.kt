package me.mkbaka.atziluth.internal.module.hook.mythicmobs.script

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class ProxyScriptMechanic(val names: List<String>) {

    // 给 js 用 不需要考虑类型
    var castEntityCallback: (Any, Any, Map<String, Any>) -> Unit = { _, _, _ -> }

    var castLocationCallback: (Any, Any, Map<String, Any>) -> Unit = { _, _, _ -> }

    // 方便js更改
    fun castAtEntity(callback: (Any, Any, Map<String, Any>) -> Unit): ProxyScriptMechanic {
        this.castEntityCallback = callback
        return this
    }

    // 方便js更改
    fun castAtLocation(callback: (Any, Any, Map<String, Any>) -> Unit): ProxyScriptMechanic {
        this.castLocationCallback = callback
        return this
    }

    fun register() {
        names.forEach { name ->
            AbstractMythicMobsHooker.scriptMechanics.computeIfAbsent(name) { this }
        }
        console().sendLang("register-mechanic", Atziluth.prefix, names[0])
    }

}
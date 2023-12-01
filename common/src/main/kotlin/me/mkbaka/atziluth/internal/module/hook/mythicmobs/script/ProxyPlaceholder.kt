package me.mkbaka.atziluth.internal.module.hook.mythicmobs.script

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class ProxyPlaceholder(val names: List<String>) {

    var callback: (Map<String, Any>) -> String = { _, -> "NotImplemented" }

    fun onRequest(callback: (Map<String, Any>) -> String): ProxyPlaceholder {
        this.callback = callback
        return this
    }

    fun register() {
        Atziluth.hookerManager.mythicMobsHooker.registerPlaceholder(this)
        names.forEach { name ->
            AbstractMythicMobsHooker.scriptPlaceholders.computeIfAbsent(name) { this }
        }
        console().sendLang("register-placeholder", Atziluth.prefix, names[0])
    }

}
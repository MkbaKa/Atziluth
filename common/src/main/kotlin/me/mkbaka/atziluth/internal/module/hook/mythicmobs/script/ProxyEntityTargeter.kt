package me.mkbaka.atziluth.internal.module.hook.mythicmobs.script

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import org.bukkit.entity.Entity
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class ProxyEntityTargeter(val names: List<String>) {

    // 给 js 用 不需要考虑类型
    var castEntityCallback: (Map<String, Any>) -> HashSet<Entity> = { _ -> hashSetOf() }

    // 方便js更改
    fun getEntities(callback: (Map<String, Any>) -> HashSet<Entity>): ProxyEntityTargeter {
        this.castEntityCallback = callback
        return this
    }

    fun register() {
        names.forEach { name ->
            AbstractMythicMobsHooker.scriptEntityTargeters.computeIfAbsent(name) { this }
        }
        console().sendLang("register-targeters", Atziluth.prefix, names[0])
    }

}
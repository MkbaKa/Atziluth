package me.mkbaka.atziluth.internal.module.hook.mythicmobs.script

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import org.bukkit.Location
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

class ProxyLocationTargeter(val names: List<String>) {

    var getLocationCallback:  (Map<String, Any>) -> HashSet<Location> = { _ -> hashSetOf() }

    // 方便js更改
    fun getLocations(callback: (Map<String, Any>) -> HashSet<Location>): ProxyLocationTargeter {
        this.getLocationCallback = callback
        return this
    }

    fun register() {
        names.forEach { name ->
            AbstractMythicMobsHooker.scriptLocationTargeters.computeIfAbsent(name) { this }
        }
        console().sendLang("register-targeters", Atziluth.prefix, names[0])
    }

}
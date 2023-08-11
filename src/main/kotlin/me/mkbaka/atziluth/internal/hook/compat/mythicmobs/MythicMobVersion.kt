package me.mkbaka.atziluth.internal.hook.compat.mythicmobs

import org.bukkit.Bukkit

enum class MythicMobVersion(val mainClass: String, val subVersion: Int) {

    IV("io.lumine.xikage.mythicmobs.MythicMobs", 11),
    V("io.lumine.mythic.bukkit.MythicBukkit", 2);

    companion object {

        val version by lazy {
            var result: MythicMobVersion? = null
            values().forEach { version ->
                try {
                    Class.forName(version.mainClass)
                    result = version
                } catch (_: ClassNotFoundException) {}
            }
            result
        }

        val subVersion by lazy {
            val pluginManager = Bukkit.getPluginManager()
            if (!pluginManager.isPluginEnabled("MythicMobs")) return@lazy -1
            val plugin = Bukkit.getPluginManager().getPlugin("MythicMobs")!!
            plugin.description.version.split(".").getOrNull(1)?.toInt() ?: -1
        }

    }
}
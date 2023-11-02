package me.mkbaka.atziluth.internal.module.hook

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.module.hook.placeholderapi.PlaceholderAPIHooker
import org.bukkit.Bukkit
import taboolib.common.platform.function.console
import taboolib.common5.cint
import taboolib.module.lang.sendLang

object HookerManager {

    lateinit var mythicMobsHooker: AbstractMythicMobsHooker

    lateinit var placeholderAPIHooker: PlaceholderAPIHooker

    fun init() {
        initMythicMobsHooker()
        initPlaceholderAPIHooker()
    }

    private fun initPlaceholderAPIHooker() {
        if (!AttributeManagerComponent.release) return
        val plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") ?: return
        placeholderAPIHooker = PlaceholderAPIHooker().apply {
            this.expansion.register()
        }
        console().sendLang("find-plugin", Atziluth.prefix, plugin.name, plugin.description.version)
    }

    private fun initMythicMobsHooker() {
        val plugin = Bukkit.getPluginManager().getPlugin("MythicMobs") ?: return

        val version = plugin.description.version
        val subVersion = version[2].cint
        val packageName = "me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl"

        kotlin.runCatching {
            mythicMobsHooker = when (version[0]) {
                // 4.x
                '4' -> {
                    when {
                        // 4.10+
                        subVersion == 1 && version[3] != '.' -> Class.forName("$packageName.MythicMobsHookerImpl411")
                        else -> Class.forName("$packageName.MythicMobsHookerImpl472")
                    }
                }
                // 5.x
                '5' -> {
                    when {
                        // 5.4.x
                        subVersion >= 4 -> Class.forName("$packageName.MythicMobsHookerImpl544")
                        else -> null
                    }
                }
                else -> null
            }?.newInstance() as? AbstractMythicMobsHooker ?: error("未支持的 MythicMobs 版本.")

            console().sendLang("find-plugin", Atziluth.prefix, plugin.name, version)
        }
    }

}
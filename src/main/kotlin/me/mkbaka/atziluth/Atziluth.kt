package me.mkbaka.atziluth

import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.configuration.ConfigManager
import me.mkbaka.atziluth.internal.register.AttributeFactory
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

object Atziluth : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    val prefix by lazy { "&8[&9Atzi&3luth&8]".colored() }

    val namespaces by lazy { listOf("Atziluth") }

    lateinit var attributePlugin: AttributePlugin

    lateinit var attributeFactory: AttributeFactory<*>

    lateinit var attributeBridge: AttributeBridge

    var isStart = true

    override fun onEnable() {
        console().sendLang("plugin-enable", prefix, Bukkit.getBukkitVersion())

        val pluginManager = Bukkit.getPluginManager()

        attributePlugin = AttributePlugin.values().firstOrNull {
            pluginManager.isPluginEnabled(it.pluginName) && it.condition(pluginManager.getPlugin(it.pluginName)!!)
        } ?: error("Not found can support plugin.")

        attributeFactory = attributePlugin.impl

        attributeBridge = attributePlugin.bridge

        Reloadable.scan()
        ConfigManager.enable()

        isStart = false
    }

}
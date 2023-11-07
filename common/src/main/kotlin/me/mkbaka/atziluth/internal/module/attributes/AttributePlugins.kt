package me.mkbaka.atziluth.internal.module.attributes

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

enum class AttributePlugins(val pluginName: String, val hookerClass: String, val check: (plugin: Plugin, version: String) -> Boolean) {
    ATTRIBUTE_PLUS("AttributePlus", "attributeplus.v3.AttributePlusHookerImpl", { _, version -> version[0] == '3' });
//    LEGACY_ATTRIBUTE_PLUS("AttributePlus", { plugin, version -> version[0] == '2' }),
//    SX_ATTRIBUTE("SX-Attribute", { plugin, version -> version[0] == '3' }),
//    LEGACY_SX_ATTRIBUTE("SX-Attribute", { plugin, version -> version[0] == '2' });

    companion object {

        fun find(): AttributePlugins? {
            return values().firstOrNull { attributePlugin ->
                val plugin = Bukkit.getPluginManager().getPlugin(attributePlugin.pluginName)!!
                attributePlugin.check(plugin, plugin.description.version)
            }
        }

    }
}
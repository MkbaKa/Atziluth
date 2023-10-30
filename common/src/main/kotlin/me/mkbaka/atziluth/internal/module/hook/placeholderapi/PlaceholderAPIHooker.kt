package me.mkbaka.atziluth.internal.module.hook.placeholderapi

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
import org.bukkit.entity.Player

class PlaceholderAPIHooker {

    val expansion by lazy {
        object : PlaceholderExpansion() {
            override fun getIdentifier(): String {
                return "Atziluth"
            }

            override fun getAuthor(): String {
                return "Mkbakaa"
            }

            override fun getVersion(): String {
                return "1.0.0"
            }

            override fun persist(): Boolean {
                return true
            }

            override fun onPlaceholderRequest(player: Player?, params: String): String {
                player!!
                val args = params.split(":")
                val str = args.getOrNull(0) ?: return "格式错误."

                val attr = AttributeManagerComponent.attributes.values.firstOrNull { attr ->
                    attr.attributeName == str || attr.placeholder == str
                } ?: return "无法根据 $str 获取属性值."

                val valueType = AttributeValueType.of(args.getOrNull(1) ?: "RANDOM") ?: AttributeValueType.RANDOM
                return player.getAttrValue(attr.attributeName, valueType).toString()
            }
        }
    }

    fun parse(player: Player, str: String): String {
        return PlaceholderAPI.setPlaceholders(player, str)
    }

    fun parse(player: Player, strs: Collection<String>): Collection<String> {
        return strs.map { str -> parse(player, str) }
    }

}
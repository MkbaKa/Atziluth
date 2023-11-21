package me.mkbaka.atziluth.internal.module.hook.placeholderapi

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.utils.Util.getOrDef
import me.mkbaka.atziluth.utils.enumOf
import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common5.eqic
import taboolib.module.lang.sendLang
import java.util.concurrent.ConcurrentHashMap

class PlaceholderAPIHooker {

    val placeholders = ConcurrentHashMap<String, PlaceholderExpansion>()

    /**
     * Atziluth 本体注册的 PAPI 扩展
     */
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

            override fun onPlaceholderRequest(player: Player, params: String): String {
                val args = params.split(":")
                val str = args.getOrNull(0) ?: return "格式错误."

                val attr = AttributeManagerComponent.attributes.values.firstOrNull { attr ->
                    attr.attributeName == str || attr.placeholder.eqic(str)
                } ?: return "无法根据 $str 获取属性."

                val valueType = enumOf<AttributeValueType>(args.getOrDef(1, "RANDOM"), def = AttributeValueType.RANDOM)
                return player.getAttrValue(attr.attributeName, valueType).toString()
            }
        }

    }

    /**
     * 转换字符串中的 PAPI 变量
     * @param [player] 玩家
     * @param [str] 字符串
     * @return [String]
     */
    fun parse(player: Player, str: String): String {
        return PlaceholderAPI.setPlaceholders(player, str)
    }

    /**
     * 转换字符串列表中的 PAPI 变量
     * @param [player] 玩家
     * @param [strs] 字符串列表
     * @return [Collection<String>]
     */
    fun parse(player: Player, strs: Collection<String>): Collection<String> {
        return strs.map { str -> parse(player, str) }
    }

    companion object {

        @SubscribeEvent
        fun reload(e: AtziluthReloadEvent) {
            if (e.status == ReloadStatus.PRE) {
                Atziluth.hookerManager.placeholderAPIHooker.placeholders.forEach { (identifier, exp) ->
                    exp.unregister()
                    console().sendLang("unregister-expansion", prefix, identifier)
                }
            }
        }

    }

}
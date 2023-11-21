package me.mkbaka.atziluth.internal.module.hook.placeholderapi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.Atziluth.prefix
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * js注册用的PAPI扩展
 */
class ProxyExpansion(val identifier: String) {

    /**
     * 变量作者
     */
    var author = "Atziluth:Script:$identifier"

    /**
     * 版本号
     */
    var version = "1.0.0"

    /**
     * 通过 在线的玩家 转换变量
     */
    var onRequest: (Player, String) -> String = { _, _ -> "" }

    fun setAuthor(author: String): ProxyExpansion {
        this.author = author
        return this
    }

    fun setVersion(version: String): ProxyExpansion {
        this.version = version
        return this
    }

    fun onRequest(callback: (Player, String) -> String): ProxyExpansion {
        this.onRequest = callback
        return this
    }

    fun register(): PlaceholderExpansion {
        return build().apply {
            Atziluth.hookerManager.placeholderAPIHooker.placeholders.compute(this.identifier) { _, oldExp ->
                oldExp?.unregister()
                this.register()
                console().sendLang("register-expansion", prefix, identifier)
                this
            }
        }
    }

    private fun build(): PlaceholderExpansion {
        return object : PlaceholderExpansion() {

            override fun getIdentifier(): String {
                return this@ProxyExpansion.identifier
            }

            override fun getAuthor(): String {
                return this@ProxyExpansion.author
            }

            override fun getVersion(): String {
                return this@ProxyExpansion.version
            }

            override fun onPlaceholderRequest(player: Player, params: String): String {
                return try {
                    this@ProxyExpansion.onRequest(player, params)
                } catch (e: Throwable) {
                    if (e.localizedMessage.startsWith("class org.openjdk.nashorn.internal.runtime.Undefined cannot be cast to class java.lang.String")) {
                        "§fonRequest 函数§c无返回值."
                    } else {
                        e.printStackTrace()
                        "§c执行时遇到异常, 详细日志已输出到后台 : §f${e.localizedMessage}"
                    }
                }
            }

        }
    }
}
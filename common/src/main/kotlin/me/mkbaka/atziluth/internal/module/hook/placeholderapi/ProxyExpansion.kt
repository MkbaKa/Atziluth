package me.mkbaka.atziluth.internal.module.hook.placeholderapi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.mkbaka.atziluth.Atziluth.prefix
import org.bukkit.OfflinePlayer
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
    var onRequest: (Player, String) -> Any = { _, _ -> "" }

    /**
     * 通过 离线玩家 转换变量
     */
    var onRequestOfflinePlayer: (OfflinePlayer, String) -> Any = { _, _ -> ""}

    fun setAuthor(author: String): ProxyExpansion {
        this.author = author
        return this
    }

    fun setVersion(version: String): ProxyExpansion {
        this.version = version
        return this
    }

    fun onRequest(callback: (Player, String) -> Any): ProxyExpansion {
        this.onRequest = callback
        return this
    }

    fun onRequestOfflinePlayer(callback: (OfflinePlayer, String) -> Any): ProxyExpansion {
        this.onRequestOfflinePlayer = callback
        return this
    }

    fun register(): PlaceholderExpansion {
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
                    this@ProxyExpansion.onRequest(player, params).toString()
                } catch (e: Throwable) {
                    if (e.localizedMessage.startsWith("class org.openjdk.nashorn.internal.runtime.Undefined cannot be cast to class java.lang.String")) {
                        "§fonRequest 函数§c无返回值."
                    } else {
                        "§c执行时遇到异常: §f${e.localizedMessage}"
                    }
                }
            }

            override fun onRequest(player: OfflinePlayer, params: String): String {
                return try {
                    this@ProxyExpansion.onRequestOfflinePlayer(player, params).toString()
                } catch (e: Throwable) {
                    if (e.localizedMessage.startsWith("class org.openjdk.nashorn.internal.runtime.Undefined cannot be cast to class java.lang.String")) {
                        "§fonRequest 函数§c无返回值."
                    } else {
                        "§c执行时遇到异常: §f${e.localizedMessage}"
                    }
                }
            }

        }.apply {
            register()
            console().sendLang("register-expansion", prefix, identifier)
        }
    }
}
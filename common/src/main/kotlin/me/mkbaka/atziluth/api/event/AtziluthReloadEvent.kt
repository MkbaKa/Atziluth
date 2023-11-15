package me.mkbaka.atziluth.api.event

import taboolib.platform.type.BukkitProxyEvent


/**
 * 插件重载事件
 * @param [status] 重载状态
 */
class AtziluthReloadEvent(val status: ReloadStatus) : BukkitProxyEvent()

enum class ReloadStatus {
    PRE, CONFIG, POST
}
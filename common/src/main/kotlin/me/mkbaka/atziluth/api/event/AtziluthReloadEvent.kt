package me.mkbaka.atziluth.api.event

import taboolib.platform.type.BukkitProxyEvent

class AtziluthReloadEvent(val status: ReloadStatus) : BukkitProxyEvent()

enum class ReloadStatus {
    PRE, CONFIG, POST
}
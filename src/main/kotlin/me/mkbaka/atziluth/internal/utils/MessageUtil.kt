package me.mkbaka.atziluth.internal.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.ComponentText
import taboolib.module.chat.Components

object MessageUtil {

    fun sendActionBar(sender: CommandSender, msg: String) {
        if (sender is Player) {
            adaptPlayer(sender).sendActionBar(msg)
        }
    }

    fun buildJson(): ComponentText {
        return Components.empty()
    }

}
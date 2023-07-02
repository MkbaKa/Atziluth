package me.mkbaka.atziluth.internal.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.chat.ComponentText
import taboolib.module.chat.Components

object MessageUtil {

    /**
     * 发送Actionbar消息
     * @param [sender] 发送方
     * @param [msg] 内容
     */
    fun sendActionBar(sender: CommandSender, msg: String) {
        if (sender is Player) {
            adaptPlayer(sender).sendActionBar(msg)
        }
    }

    /**
     * 创造一个Json构建器
     * @return [ComponentText]
     */
    fun buildJson(): ComponentText {
        return Components.empty()
    }

}
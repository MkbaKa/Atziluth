package me.mkbaka.atziluth.internal.utils

import org.bukkit.entity.Player
import taboolib.common5.cdouble
import taboolib.platform.compat.replacePlaceholder

object CompatUtil {

    fun parse(player: Player, placeholder: String): String {
        return placeholder.replacePlaceholder(player)
    }

    fun check(player: Player, placeholder: String, target: Double): Boolean {
        return placeholder.replacePlaceholder(player).cdouble >= target
    }

}
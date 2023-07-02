package me.mkbaka.atziluth.internal.utils

import org.bukkit.entity.Player
import taboolib.common5.cdouble
import taboolib.platform.compat.replacePlaceholder

object CompatUtil {

    /**
     * 解析字符串内的PlaceholderAPI变量
     * @param [player] 玩家
     * @param [placeholder] 字符串
     * @return [String]
     */
    fun parse(player: Player, placeholder: String): String {
        return placeholder.replacePlaceholder(player)
    }

    /**
     * 判断该变量值是否大于等于指定数值
     * @param [player] 玩家
     * @param [placeholder] 字符串
     * @param [target] 目标数值
     * @return [Boolean]
     */
    fun check(player: Player, placeholder: String, target: Double): Boolean {
        return placeholder.replacePlaceholder(player).cdouble >= target
    }

}
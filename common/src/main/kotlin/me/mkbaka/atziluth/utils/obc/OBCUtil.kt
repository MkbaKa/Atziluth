package me.mkbaka.atziluth.utils.obc

import org.bukkit.Bukkit
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.MinecraftVersion

object OBCUtil {

    fun syncCommands() {
        if (MinecraftVersion.isHigherOrEqual(5)) {
            Bukkit.getServer().invokeMethod<Unit>("syncCommands", findToParent = false)
        }
    }

}
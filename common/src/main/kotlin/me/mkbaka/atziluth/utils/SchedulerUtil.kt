package me.mkbaka.atziluth.utils

import me.mkbaka.atziluth.Atziluth.plugin
import org.bukkit.Bukkit

object SchedulerUtil {

    private val scheduler by lazy { Bukkit.getScheduler() }

    fun callSync(func: () -> Unit) {
        if (Bukkit.isPrimaryThread()) func() else scheduler.callSyncMethod(plugin, func)
    }

    fun callAsync(func: () -> Unit) {
        if (Bukkit.isPrimaryThread()) scheduler.runTaskAsynchronously(plugin, func) else func()
    }

}
package me.mkbaka.atziluth.internal.utils

import me.mkbaka.atziluth.Atziluth.plugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

private val scheduler by lazy { Bukkit.getScheduler() }

fun callSync(callback: () -> Unit) {
    if (Bukkit.isPrimaryThread()) callback() else scheduler.callSyncMethod(plugin, callback)
}

fun callAsync(callback: () -> Unit) {
    if (!Bukkit.isPrimaryThread()) callback() else scheduler.runTaskAsynchronously(plugin, callback)
}

fun callSyncLater(delay: Long, callback: () -> Unit): BukkitTask {
    return scheduler.runTaskLater(plugin, callback, delay)
}

fun callAsyncLater(delay: Long, callback: () -> Unit): BukkitTask {
    return scheduler.runTaskLaterAsynchronously(plugin, callback, delay)
}

fun callSyncTimer(delay: Long, period: Long, callback: () -> Unit): BukkitTask {
    return scheduler.runTaskTimer(plugin, callback, delay, period)
}

fun callAsyncTimer(delay: Long, period: Long, callback: () -> Unit): BukkitTask {
    return scheduler.runTaskTimerAsynchronously(plugin, callback, delay, period)
}
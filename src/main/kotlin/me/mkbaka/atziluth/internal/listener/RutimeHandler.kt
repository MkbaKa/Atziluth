package me.mkbaka.atziluth.internal.listener

import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.configuration.AttributeManager
import me.mkbaka.atziluth.internal.configuration.ConfigManager
import me.mkbaka.atziluth.internal.register.AttributeType
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor

object RutimeHandler {

    // 调度器
    private lateinit var task: PlatformExecutor.PlatformTask

    // 执行到的时间 每60s重置一次
    private var timed: Long = 0

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        task = submitAsync(period = 20) {
            if (++timed > ConfigManager.runtimeResetPeriod) timed = 1

            val attrs = AttributeManager.subAttributes.filter { (_, attr) ->
                attr.type == AttributeType.RUNTIME && attr.runtimeCallback != null && attr.period != -1L && timed % attr.period == 0L
            }

            Bukkit.getOnlinePlayers().forEach { player ->
                attrs.forEach { (key, attr) ->
                    // 用filter会再创建个新的map  所以直接用if了
                    if (attr.skipFilter || AttributeBridge.getAttrValue(player, key) > 0.0) {
                        attr.runtimeCallback?.let { it(attr, player) }
                    }
                }
            }

        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        if (this::task.isInitialized) task.cancel()
    }

}
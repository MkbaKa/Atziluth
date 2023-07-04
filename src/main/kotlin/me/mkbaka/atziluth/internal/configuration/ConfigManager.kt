package me.mkbaka.atziluth.internal.configuration

import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.listener.ListenerManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object ConfigManager {

    @Config("config.yml")
    lateinit var config: ConfigFile

    var runtimeResetPeriod: Long = 60

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        config.onReload {
            runtimeResetPeriod = config.getLong("Settings.runtimeResetPeriod", 60)
        }
    }

    fun reload() {
        config.reload()
        ListenerManager.unRegisterAllListener()
        Reloadable.reloadAll()
    }

}
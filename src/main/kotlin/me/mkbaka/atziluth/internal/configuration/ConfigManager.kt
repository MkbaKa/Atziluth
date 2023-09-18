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
    val clearAttributeWhiteList = HashSet<String>()

    var checkTaskPeriod: Long = 20 * 60 * 30
    var tempDataTimeout: Long = 1000 * 60 * 30

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        config.onReload {
            runtimeResetPeriod = config.getLong("Settings.runtimeResetPeriod", 60)
            clearAttributeWhiteList.clear()
            clearAttributeWhiteList.addAll(config.getStringList("Settings.clearAttributeWhiteList"))

            checkTaskPeriod = 20 * 60 * config.getLong("TempData.taskPeriod", 30)
            tempDataTimeout = 1000 * 60 * config.getLong("TempData.timeout", 30)
        }
        reload()
    }

    fun reload() {
        config.reload()
        ListenerManager.unRegisterAllListener()
        Reloadable.reloadAll()
    }

}
package me.mkbaka.atziluth.internal.configuration

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object ConfigurationManager {

    @Config("config.yml")
    lateinit var config: ConfigFile

    @Config("script.yml")
    lateinit var script: ConfigFile

    var runtimeResetPeriod = 60

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        config.onReload {
            this.runtimeResetPeriod = config.getInt("Settings.runtimeResetPeriod", 60)
        }
        reload()
    }

    fun reload() {
        config.reload()
        AbstractConfigComponent.reloadAll()
    }

}
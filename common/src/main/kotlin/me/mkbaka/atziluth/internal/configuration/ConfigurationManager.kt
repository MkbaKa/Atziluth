package me.mkbaka.atziluth.internal.configuration

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object ConfigurationManager {

    @Config("config.yml")
    lateinit var config: ConfigFile

    // runtime 属性计时器重置间隔 (s)
    var runtimeResetPeriod: Int = 60

    // 清除属性时的基础白名单
    val clearAttributeWhiteList = ArrayList<String>()

    // tempData 检测间隔 (tick)
    var tempDataChecker: Long = 60 * 30 * 20

    // tempData 超时时间 (m)
    var tempDataTimeout: Int = 60 * 30

    // tempAttribute 检测间隔 (tick)
    var tempAttributeChecker = 20

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        config.onReload {
            clearAttributeWhiteList.clear()
            clearAttributeWhiteList.addAll(config.getStringList("Settings.clearAttributeWhiteList"))
            this.runtimeResetPeriod = config.getInt("Settings.runtimeResetPeriod", 60)
            this.tempDataChecker = config.getLong("TempData.taskPeriod", 30) * 60 * 20
            this.tempDataTimeout = config.getInt("TempData.timeout", 30) * 60 * 1000
            this.tempAttributeChecker = config.getInt("TempData.attributeChecker", 20)
        }
        reload()
    }

    fun reload() {
        config.reload()
        AbstractConfigComponent.reloadAll()
    }

}
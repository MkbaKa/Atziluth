package me.mkbaka.atziluth.internal.configuration

import me.mkbaka.atziluth.api.event.AtziluthReloadEvent
import me.mkbaka.atziluth.api.event.ReloadStatus
import me.mkbaka.atziluth.utils.FileUtil.releaseResource
import me.mkbaka.atziluth.utils.map.PriorityMap
import taboolib.common.platform.event.SubscribeEvent
import java.io.File

abstract class AbstractConfigComponent(priority: Int) {

    init {
        if (this.release && !this.folder.exists()) {
            if (this.defaultConfigs.isNotEmpty()) {
                this.defaultConfigs.forEach { config ->
                    releaseResource(this.folder, config)
                }
            } else {
                releaseResource(this.folder, null)
            }
        }

        components[priority] = this
    }

    /**
     * 是否生成文件
     */
    abstract val release: Boolean

    /**
     * 所属文件夹
     */
    abstract val folder: File

    /**
     * 重载
     */
    abstract fun reload()

    /**
     * 默认的配置文件列表
     */
    open val defaultConfigs: List<String>
        get() = emptyList()

    companion object {

        /**
         * 所有已注册的配置组件
         */
        private val components = PriorityMap<AbstractConfigComponent>()

        /**
         * 重载所有配置
         */
        fun reloadAll() {
            AtziluthReloadEvent(ReloadStatus.CONFIG).call()
        }

        /**
         * 重载指定优先级的配置
         */
        fun reload(index: Int) {
            components[index]?.reload()
        }

        @SubscribeEvent
        fun reload(e: AtziluthReloadEvent) {
            if (e.status == ReloadStatus.CONFIG) {
                components.forEach { (_, component) ->
                    component.reload()
                }
            }
        }

    }

}
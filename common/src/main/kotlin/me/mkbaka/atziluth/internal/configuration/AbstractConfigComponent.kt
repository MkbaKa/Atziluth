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

    abstract val release: Boolean

    abstract val folder: File

    abstract fun reload()

    open val defaultConfigs: List<String>
        get() = emptyList()

    companion object {

        val components = PriorityMap<AbstractConfigComponent>()

        fun reloadAll() {
            AtziluthReloadEvent(ReloadStatus.CONFIG).call()
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
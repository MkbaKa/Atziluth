package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.internal.module.script.Script
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object ScriptsComponent : AbstractConfigComponent(8) {

    override val release: Boolean
        get() = true

    override val folder: File
        get() = newFolder(getDataFolder(), "scripts", create = false)

    val scripts = ConcurrentHashMap<String, Script>()

    override fun reload() {
        AbstractMythicMobsHooker.apply {
            scriptMechanics.clear()
            scriptConditions.clear()
            scriptLocationTargeters.clear()
            scriptEntityTargeters.clear()
        }
        folder.executeSubFiles { file ->
            val script = AbstractScriptFactory.compile(file) ?: return@executeSubFiles
            scripts[file.path.substringAfter("${folder.name}${File.separator}")] = script.apply {
                if (isFunction("onLoad")) invokeFunction("onLoad")
            }
        }
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        scripts.forEach { (path, script) ->
            if (script.isFunction("onEnable")) script.invokeFunction("onEnable")
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        scripts.forEach { (path, script) ->
            if (script.isFunction("onDisable")) script.invokeFunction("onDisable")
        }
    }

}
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
            scriptPlaceholders.clear()
            scriptMechanics.clear()
            scriptConditions.clear()
            scriptLocationTargeters.clear()
            scriptEntityTargeters.clear()
        }
        folder.executeSubFiles { file ->
            val script = AbstractScriptFactory.compile(file) ?: return@executeSubFiles
            scripts[file.path.substringAfter("${folder.name}${File.separator}")] = script.apply {
                invoke(this, "onLoad")
            }
        }
    }

    fun enable() {
        scripts.forEach { (path, script) ->
            invoke(script, "onEnable")
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        scripts.forEach { (path, script) ->
            invoke(script, "onDisable")
        }
    }

    fun invoke(script: Script, func: String, args: Map<String, Any> = emptyMap()) {
        if (script.isFunction(func)) {
            try {
                script.invokeFunction(func, args)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
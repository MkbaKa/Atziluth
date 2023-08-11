package me.mkbaka.atziluth.internal.configuration

import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.scripts.AbstractScript
import me.mkbaka.atziluth.internal.utils.FileUtil
import me.mkbaka.atziluth.internal.utils.FileUtil.executeSubFiles
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import java.util.concurrent.ConcurrentHashMap

object ScriptManager : Reloadable(priority = 7) {

    val folder by lazy {
        FileUtil.newFolder(
            getDataFolder(),
            "scripts"
        ) { file ->
            releaseResourceFile("${file.name}/example.js")
        }
    }

    val scripts = ConcurrentHashMap<String, AbstractScript>()

    override fun reload() {
        scripts.clear()

        folder.executeSubFiles {
            if (it.extension != "js" && it.extension != "yml") return@executeSubFiles
            scripts[it.path] = AbstractScript.buildScript(it).apply { onLoad() }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        scripts.forEach { (path, script) ->
            script.onEnable()
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        scripts.forEach { (path, script) ->
            script.onDisable()
        }
    }

    fun invoke(script: AbstractScript, func: String, map: Map<String, Any>, vararg args: Any): Any? {
        return script.reader.invoke(func, map, args)
    }

}
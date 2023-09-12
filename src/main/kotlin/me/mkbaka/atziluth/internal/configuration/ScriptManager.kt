package me.mkbaka.atziluth.internal.configuration

import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.scriptreader.AbstractScriptReader
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

    val scripts = ConcurrentHashMap<String, AbstractScriptReader>()

    override fun reload() {
        scripts.clear()

        folder.executeSubFiles {
            if (it.extension != "js" && it.extension != "yml") return@executeSubFiles
            scripts[it.path.substringAfter("scripts\\")] =
                AbstractScriptReader.create(it).apply { this.invoke("onLoad") }
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        scripts.forEach { (path, script) ->
            script.invoke("onEnable")
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        scripts.forEach { (path, script) ->
            script.invoke("onDisable")
        }
    }


}
package me.mkbaka.atziluth.internal.hook.nashorn

import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.utils.ClassUtil
import me.mkbaka.atziluth.internal.utils.FileUtil.executeSubFiles
import taboolib.common.io.newFile
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.platform.util.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap

object ScriptLibsLoader : Reloadable(priority = 1) {

    private val folder by lazy { newFile(bukkitPlugin.dataFolder, "libs", create = true, folder = true) }

    lateinit var libStr: String

    val script = Configuration.loadFromFile(releaseResourceFile("script.yml", replace = false), type = Type.YAML)
    val classMapper = ConcurrentHashMap<String, Any>()

    init {
        script.onReload {
            script.getKeys(false).forEach {
                classMapper[it] = ClassUtil.staticClass(script.getString(it)!!)
            }
        }

        releaseResourceFile("${folder.name}/basic.js", replace = false)
        releaseResourceFile("${folder.name}/util.js", replace = false)
        releaseResourceFile("${folder.name}/util/item.js", replace = false)
        releaseResourceFile("${folder.name}/util/message.js", replace = false)
        releaseResourceFile("${folder.name}/util/string.js", replace = false)
    }

    override fun reload() {
        classMapper.clear()
        script.reload()

        val builder = StringBuilder()

        folder.executeSubFiles { file ->
            if (file.extension != "js") return@executeSubFiles
            builder
                .append(file.readText())
                .append("\n")
        }

        libStr = builder.toString()
    }

}
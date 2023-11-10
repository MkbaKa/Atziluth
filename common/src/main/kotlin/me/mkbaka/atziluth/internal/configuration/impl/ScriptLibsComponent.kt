package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.utils.ClassUtil
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object ScriptLibsComponent : AbstractConfigComponent(7) {

    override val release: Boolean
        get() = true

    override val folder: File
        get() = newFolder(getDataFolder(), "libs", create = false)

    @Config
    lateinit var script: ConfigFile

    val staticClasses = ConcurrentHashMap<String, Any>()
    val scriptLibFiles = HashSet<File>()

    override fun reload() {
        staticClasses.clear()
        scriptLibFiles.clear()
        script.reload()
        script.getKeys(false).forEach { name ->
            staticClasses.computeIfAbsent(name) { _, ->
                ClassUtil.staticClass(script.getString(name)!!)
            }
        }
        folder.executeSubFiles { file ->
            if (file.extension != "js") return@executeSubFiles
            scriptLibFiles.add(file)
        }
    }

}
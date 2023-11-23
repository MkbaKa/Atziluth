package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import java.io.File

object ScriptLibsComponent : AbstractConfigComponent(7) {

    override val release: Boolean
        get() = true

    override val folder: File
        get() = newFolder(getDataFolder(), "libs", create = false)

    @Config("script.yml")
    lateinit var script: ConfigFile

    val scriptLibFiles = HashSet<File>()
    val stringLibs = HashSet<String>()

    override fun reload() {
        scriptLibFiles.clear()
        stringLibs.clear()
        script.reload()
        script.getKeys(false).forEach { name ->
            stringLibs.add("const $name = Java.type(\"${script.getString(name)!!}\")")
        }
        folder.executeSubFiles { file ->
            if (file.extension != "js") return@executeSubFiles
            scriptLibFiles.add(file)
            stringLibs.add(file.readText())
        }
    }

}
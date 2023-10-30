package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import java.io.File

object ScriptLibsComponent : AbstractConfigComponent(7) {

    override val release: Boolean
        get() = true

    override val folder: File
        get() = newFolder(getDataFolder(), "libs", create = false)

    val scriptLibFiles = HashSet<File>()

    override fun reload() {
        scriptLibFiles.clear()
        folder.executeSubFiles { file ->
            if (file.extension != "js") return@executeSubFiles
            scriptLibFiles.add(file)
        }
    }

}
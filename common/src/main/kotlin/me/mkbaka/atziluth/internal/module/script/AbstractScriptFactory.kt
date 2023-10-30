package me.mkbaka.atziluth.internal.module.script

import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractScriptFactory : AbstractConfigComponent(1) {

    init {
        this.prefixes.forEach { prefix -> prefixFactories.computeIfAbsent(prefix) { this } }
        this.suffixes.forEach { suffix -> suffixFactories.computeIfAbsent(suffix) { this } }
    }

    abstract val prefixes: Array<String>

    abstract val suffixes: Array<String>

    abstract fun compileScript(script: String): Script

    abstract fun compileScript(file: File): Script

    override val folder: File
        get() = newFolder(getDataFolder(), "scripts", create = false)

    override val release: Boolean
        get() = true

    companion object {

        private val prefixFactories = ConcurrentHashMap<String, AbstractScriptFactory>()
        private val suffixFactories = ConcurrentHashMap<String, AbstractScriptFactory>()

        /**
         * 根据文件后缀编译
         * @param [file] 文件
         * @return [Script?]
         */
        fun compile(file: File): Script? {
            return suffixFactories[file.extension]?.compileScript(file)
        }

        /**
         * 根据字符串前缀编译
         * @param [script] 字符串
         * @return [Script?]
         */
        fun compile(script: String): Script? {
            prefixFactories.forEach { entry ->
                if (script.startsWith(entry.key)) return entry.value.compileScript(script.removePrefix(entry.key))
            }
            return null
        }

    }

}
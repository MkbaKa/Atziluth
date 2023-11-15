package me.mkbaka.atziluth.internal.module.script.javascript.impl.factory

import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.internal.module.script.Script
import me.mkbaka.atziluth.internal.module.script.javascript.AbstractCompiledJavaScript
import me.mkbaka.atziluth.internal.module.script.javascript.impl.script.CompiledJavaScript
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object JavaScriptFactoryImpl : AbstractScriptFactory() {

    private val compiledScripts = ConcurrentHashMap<Int, AbstractCompiledJavaScript>()

    override val prefixes: Array<String>
        get() = arrayOf("js", "js:")

    override val suffixes: Array<String>
        get() = arrayOf("js")

    override fun compileScript(script: String): Script {
        return compiledScripts.computeIfAbsent(script.hashCode()) {
            CompiledJavaScript(script)
        }
    }

    override fun compileScript(file: File): Script {
        return compiledScripts.computeIfAbsent(file.hashCode()) {
            file.reader().use { CompiledJavaScript(it) }
        }
    }

    override fun reload() {
        compiledScripts.clear()
    }

}
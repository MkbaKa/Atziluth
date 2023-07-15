package me.mkbaka.atziluth.internal.scriptreader.impl

import me.mkbaka.atziluth.internal.hook.nashorn.CompiledScript
import me.mkbaka.atziluth.internal.hook.nashorn.ScriptFactory
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import java.io.Reader

class JavaScriptReaderImpl : ScriptReader {

    constructor(script: String) : super(script) {
        this.compiledScript = ScriptFactory.compile(script)
    }

    constructor(reader: Reader) : super(reader) {
        this.compiledScript = ScriptFactory.compile(reader.readText())
    }

    private val compiledScript: CompiledScript

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String): T {
        return compiledScript.scriptEngine[name] as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String, def: T): T {
        return compiledScript.scriptEngine[name] as? T ?: def
    }

    override fun invoke(name: String, map: Map<String, Any>, vararg args: Any): Any {
        return compiledScript.invoke(name, map, args)
    }

    override fun isFunction(name: String): Boolean {
        return compiledScript.isFunction(name)
    }

}
package me.mkbaka.atziluth.internal.module.script.javascript

import me.mkbaka.atziluth.internal.module.script.Script
import me.mkbaka.atziluth.internal.module.script.javascript.AbstractNashornHooker.Companion.hooker
import java.io.Reader
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.ScriptEngine

abstract class AbstractCompiledJavaScript : Script {

    constructor(string: String) {
        this.scriptEngine = hooker.getScriptEngine()
        loadLibs()
        this.compiledScript = hooker.compile(scriptEngine, string)
        mirrorScriptObject()
    }

    constructor(reader: Reader) {
        this.scriptEngine = hooker.getScriptEngine()
        loadLibs()
        this.compiledScript = hooker.compile(scriptEngine, reader)
        mirrorScriptObject()
    }

    val compiledScript: CompiledScript
    val scriptEngine: ScriptEngine

    open fun loadLibs() {}

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String): T? {
        return this.scriptEngine[name] as T?
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String, def: T): T {
        return (this.scriptEngine[name] ?: def) as T
    }

    override fun isFunction(func: String): Boolean {
        return this.scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).containsKey(func)
    }

    private fun mirrorScriptObject() {
        compiledScript.eval()
        scriptEngine.eval("""
            function ObjectMirror() {}
            ObjectMirror.prototype = this
            function mirror() { return new ObjectMirror() }
        """.trimIndent())
    }
}
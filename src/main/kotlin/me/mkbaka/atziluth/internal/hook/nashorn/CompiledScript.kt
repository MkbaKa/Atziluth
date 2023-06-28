package me.mkbaka.atziluth.internal.hook.nashorn

import java.io.File
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.ScriptEngine
import me.mkbaka.atziluth.internal.hook.nashorn.AbstractNashornHooker.Companion.inst as hooker

open class CompiledScript {

    val compiledScript: CompiledScript
    val scriptEngine: ScriptEngine
    val libs: String

    constructor(script: String) {
        this.scriptEngine = hooker.getScriptEngine()
        libs = loadLib()
        this.compiledScript = hooker.compile(script)
        magicFunction()
    }

    constructor(file: File) {
        this.scriptEngine = hooker.getScriptEngine()
        libs = loadLib()
        this.compiledScript = hooker.compile(file.reader())
        magicFunction()
    }

    open fun loadLib(): String {
        return ""
    }

    fun invoke(func: String, map: Map<String, Any?> = emptyMap(), vararg args: Any): Any {
        return hooker.invoke(this, func, map, args)
    }

    fun eval(map: Map<String, Any?> = emptyMap()): Boolean {
        return hooker.eval(compiledScript, map)
    }

    fun isFunction(name: String): Boolean {
        return scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).containsKey(name)
    }

    private fun magicFunction() {
        scriptEngine.eval(
            """
                
            const Atziluth = Packages.me.mkbaka.atziluth.Atziluth.INSTANCE
            const bridge = Atziluth.attributeBridge
           
            $libs
            
            function ObjectMirror() {}
            ObjectMirror.prototype = this
            function newObject() { return new ObjectMirror() }
        """.trimIndent()
        )
    }
}
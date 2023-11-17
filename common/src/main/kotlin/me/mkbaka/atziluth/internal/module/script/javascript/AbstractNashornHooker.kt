package me.mkbaka.atziluth.internal.module.script.javascript

import me.mkbaka.atziluth.internal.module.script.javascript.impl.hooker.LegacyNashornHooker
import me.mkbaka.atziluth.internal.module.script.javascript.impl.hooker.NashornHooker
import me.mkbaka.atziluth.utils.ClassUtil
import java.io.Reader
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine
import javax.script.SimpleBindings

abstract class AbstractNashornHooker {

    abstract fun getNashornEngine(args: Array<String>): ScriptEngine

    abstract fun invoke(script: AbstractCompiledJavaScript, func: String, topLevels: Map<String, Any>, vararg args: Any): Any?

    fun getScriptEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6"))
    }

    fun getGlobalEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6", "--global-per-engine"))
    }

    fun compile(script: String): CompiledScript {
        return (getScriptEngine() as Compilable).compile(script)
    }

    fun compile(reader: Reader): CompiledScript {
        return (getScriptEngine() as Compilable).compile(reader)
    }

    fun compile(engine: ScriptEngine, script: String): CompiledScript {
        return (engine as Compilable).compile(script)
    }

    fun compile(engine: ScriptEngine, reader: Reader): CompiledScript {
        return (engine as Compilable).compile(reader)
    }

    fun eval(script: CompiledScript, args: Map<String, Any>): Any? {
        val result = try {
            script.eval(SimpleBindings(args))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return result
    }

    companion object {

        val hooker by lazy {
            when {
                ClassUtil.checkClass("jdk.nashorn.api.scripting.NashornScriptEngineFactory") -> LegacyNashornHooker
                else -> NashornHooker
            }
        }

        val globalEngine by lazy { hooker.getGlobalEngine() }

    }
}
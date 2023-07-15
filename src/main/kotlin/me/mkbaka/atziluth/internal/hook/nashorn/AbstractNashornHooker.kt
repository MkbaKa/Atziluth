package me.mkbaka.atziluth.internal.hook.nashorn

import me.mkbaka.atziluth.internal.hook.nashorn.impl.LegacyNashornHookerImpl
import me.mkbaka.atziluth.internal.hook.nashorn.impl.NashornHookerImpl
import java.io.Reader
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine
import javax.script.SimpleBindings

abstract class AbstractNashornHooker {

    /**
     * 获取Nashorn引擎
     * @param [args] 参数
     * @return [ScriptEngine]
     */
    abstract fun getNashornEngine(args: Array<String>): ScriptEngine

    /**
     * 调用函数
     * @param [script] 预编译的脚本
     * @param [func] 函数名
     * @param [map] 顶级成员
     * @param [args] 方法形参
     * @return [Any]
     */
    abstract fun invoke(
        script: me.mkbaka.atziluth.internal.hook.nashorn.CompiledScript,
        func: String,
        map: Map<String, Any?>,
        vararg args: Any
    ): Any

    /**
     * 获取脚本引擎
     * @return [ScriptEngine]
     */
    fun getScriptEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6"))
    }

    /**
     * 获取全局引擎
     * @return [ScriptEngine]
     */
    fun getGlobalEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6", "--global-per-engine"))
    }

    /**
     * 编译脚本
     * @param [script] 脚本
     * @return [CompiledScript]
     */
    fun compile(script: String): CompiledScript {
        return (getScriptEngine() as Compilable).compile(script)
    }

    /**
     * 编译
     * @param [reader] Reader
     * @return [CompiledScript]
     */
    fun compile(reader: Reader): CompiledScript {
        return (getScriptEngine() as Compilable).compile(reader)
    }

    /**
     * 运行一段脚本
     * @param [script] 脚本
     * @param [map] 参数
     * @return [Boolean]
     */
    fun eval(script: CompiledScript, map: Map<String, Any?>): Boolean {
        val result = try {
            script.eval(SimpleBindings().apply { putAll(map) })
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
        return when (result) {
            is Boolean -> result
            null -> true
            else -> false
        }
    }

    companion object {

        val inst by lazy {
            try {
                Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
                LegacyNashornHookerImpl
            } catch (e: ClassNotFoundException) {
                NashornHookerImpl
            }
        }

        val globalEngine by lazy { inst.getGlobalEngine() }

    }

}
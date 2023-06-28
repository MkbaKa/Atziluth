package me.mkbaka.atziluth.internal.hook.nashorn.impl

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.mkbaka.atziluth.internal.hook.nashorn.AbstractNashornHooker
import me.mkbaka.atziluth.internal.hook.nashorn.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine

object LegacyNashornHookerImpl : AbstractNashornHooker() {

    override fun getNashornEngine(args: Array<String>): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, this::class.java.classLoader)
    }

    override fun invoke(script: CompiledScript, func: String, map: Map<String, Any?>, vararg args: Any): Any {
        return ((script.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror).run {
            putAll(map)
            put("vars", map)
            callMember(func, *args)
        }
    }

}
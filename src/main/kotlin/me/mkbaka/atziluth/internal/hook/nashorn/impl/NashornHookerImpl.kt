package me.mkbaka.atziluth.internal.hook.nashorn.impl

import me.mkbaka.atziluth.internal.hook.nashorn.AbstractNashornHooker
import me.mkbaka.atziluth.internal.hook.nashorn.CompiledScript
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import taboolib.common.env.RuntimeDependency
import javax.script.Invocable
import javax.script.ScriptEngine

@RuntimeDependency(
    "!org.openjdk.nashorn:nashorn-core:15.4",
    test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
)
object NashornHookerImpl : AbstractNashornHooker() {

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
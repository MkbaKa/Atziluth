package me.mkbaka.atziluth.internal.module.script.javascript.impl.hooker

import me.mkbaka.atziluth.internal.module.script.javascript.AbstractCompiledJavaScript
import me.mkbaka.atziluth.internal.module.script.javascript.AbstractNashornHooker
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import javax.script.Invocable
import javax.script.ScriptEngine

object NashornHooker : AbstractNashornHooker() {

    override fun getNashornEngine(args: Array<String>): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, this::class.java.classLoader)
    }

    override fun invoke(
        script: AbstractCompiledJavaScript,
        func: String,
        topLevels: Map<String, Any>,
        vararg args: Any
    ): Any? {
        return ((script.scriptEngine as Invocable).invokeFunction("mirror") as ScriptObjectMirror).run {
            this.putAll(topLevels)
            this["vars"] = topLevels
            this.callMember(func, *args)
        }
    }

}
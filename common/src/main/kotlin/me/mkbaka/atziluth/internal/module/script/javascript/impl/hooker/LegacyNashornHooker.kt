package me.mkbaka.atziluth.internal.module.script.javascript.impl.hooker

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.mkbaka.atziluth.internal.module.script.javascript.AbstractNashornHooker
import javax.script.Invocable
import javax.script.ScriptEngine

object LegacyNashornHooker : AbstractNashornHooker() {

    override fun getNashornEngine(args: Array<String>): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, this::class.java.classLoader)
    }

    override fun invoke(
            script: me.mkbaka.atziluth.internal.module.script.javascript.AbstractCompiledJavaScript,
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
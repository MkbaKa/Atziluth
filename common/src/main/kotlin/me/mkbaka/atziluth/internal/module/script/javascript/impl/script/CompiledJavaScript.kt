package me.mkbaka.atziluth.internal.module.script.javascript.impl.script

import me.mkbaka.atziluth.internal.configuration.impl.ScriptLibsComponent
import me.mkbaka.atziluth.internal.module.script.javascript.AbstractCompiledJavaScript
import me.mkbaka.atziluth.internal.module.script.javascript.AbstractNashornHooker.Companion.hooker
import java.io.Reader

class CompiledJavaScript : AbstractCompiledJavaScript {

    constructor(string: String): super(string)

    constructor(reader: Reader): super(reader)

    override fun loadLibs() {
        val builder = StringBuilder()
        ScriptLibsComponent.scriptLibFiles.forEach { file ->
            builder.append("load(\"${file.path.replace("\\", "/")}\")\n")
        }
        this.scriptEngine.eval(builder.toString())
    }

    override fun evalScript(args: Map<String, Any>): Any? {
        return hooker.eval(super.compiledScript, args)
    }

    override fun invokeFunction(func: String, topLevels: Map<String, Any>, vararg args: Any): Any? {
        return hooker.invoke(this, func, topLevels, *args)
    }

}
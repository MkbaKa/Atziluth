package me.mkbaka.atziluth.internal.hook.nashorn

import java.io.File

class ScriptExpansion : CompiledScript {

    constructor(script: String) : super(script)

    constructor(file: File) : super(file)

    override fun loadLib(): String {
        ScriptLibsLoader.classMapper.forEach { (name, obj) ->
            scriptEngine.put(name, obj)
        }
        return ScriptLibsLoader.libStr
    }

}
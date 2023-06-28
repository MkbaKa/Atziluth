package me.mkbaka.atziluth.internal.scriptreader.impl

import me.mkbaka.atziluth.Atziluth.namespaces
import me.mkbaka.atziluth.internal.configuration.AttributeManager
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import taboolib.module.kether.Workspace
import java.io.Reader

class KetherScriptReader : ScriptReader {

    constructor(script: String) : super(script) {

    }

    constructor(reader: Reader) : super(reader) {

    }

    override fun <T> getTopLevel(name: String): T {
        error("Not yet implemented")
    }

    override fun invoke(name: String, map: Map<String, Any>, vararg args: Any): Any {
        error("Not yet implemented")
    }

    override fun isFunction(name: String): Boolean {
        error("Not yet implemented")
    }

    companion object {

        val workspace = Workspace(AttributeManager.folder, namespace = namespaces)

        fun isScriptFile(file: String): Boolean {
            return workspace.scripts.containsKey(file)
        }

    }

}
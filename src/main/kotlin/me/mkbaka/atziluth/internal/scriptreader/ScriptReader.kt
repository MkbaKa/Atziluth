package me.mkbaka.atziluth.internal.scriptreader

import me.mkbaka.atziluth.internal.scriptreader.impl.JavaScriptReaderImpl
import me.mkbaka.atziluth.internal.scriptreader.impl.KetherScriptReader
import java.io.File
import java.io.Reader

abstract class ScriptReader {

    protected val script: String

    constructor(script: String) {
        this.script = script
    }

    constructor(reader: Reader) {
        this.script = reader.readText()
    }

    abstract fun <T> getTopLevel(name: String): T

    abstract fun invoke(name: String, map: Map<String, Any>, vararg args: Any): Any

    abstract fun isFunction(name: String): Boolean

    companion object {

        fun create(file: File): ScriptReader {
            return read(file.extension, file) { file.readText() }
        }

        fun create(scriptType: ScriptType, script: String): ScriptReader {
            return read(scriptType, script) { script }
        }

        fun create(scriptType: ScriptType, reader: Reader): ScriptReader {
            return read(scriptType, reader) { reader.readText() }
        }

        fun create(scriptType: String, script: String): ScriptReader {
            return read(scriptType, script) { script }
        }

        fun create(scriptType: String, reader: Reader): ScriptReader {
            return read(scriptType, reader) { reader.readText() }
        }

        private fun <T> read(scriptType: Any, obj: T, script: (T) -> String): ScriptReader {
            return when (val type =
                if (scriptType is ScriptType) scriptType else ScriptType.of(scriptType.toString())) {
                ScriptType.JAVASCRIPT -> JavaScriptReaderImpl(script(obj))
                ScriptType.KETHER -> KetherScriptReader(script(obj))
                else -> error("Unsupported script type $type")
            }
        }

    }

}
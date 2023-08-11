package me.mkbaka.atziluth.internal.scripts

import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scriptreader.ScriptType
import me.mkbaka.atziluth.internal.scripts.impl.JavaScriptImpl
import me.mkbaka.atziluth.internal.scripts.impl.KetherScriptImpl
import org.serverct.ersha.jd.T
import java.io.File

abstract class AbstractScript(val reader: ScriptReader) {

    abstract fun onEnable()

    abstract fun onLoad()

    abstract fun onDisable()

    companion object {

        fun buildScript(file: File): AbstractScript {
            return build(file.extension, file) { ScriptReader.create(file) }
        }

        fun buildScript(script: String, type: ScriptType): AbstractScript {
            return build(type, script) { ScriptReader.create(type, script) }
        }

        private fun <T> build(scriptType: Any, obj: T, script: (T) -> ScriptReader): AbstractScript {
            return when (val type =
                if (scriptType is ScriptType) scriptType else ScriptType.of(scriptType.toString())
            ) {
                ScriptType.JAVASCRIPT -> JavaScriptImpl(script(obj))
                ScriptType.KETHER -> KetherScriptImpl(script(obj))
                else -> error("Unsupported script type $type")
            }
        }

    }

}
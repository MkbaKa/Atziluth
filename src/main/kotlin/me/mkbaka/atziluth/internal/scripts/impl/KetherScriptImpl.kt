package me.mkbaka.atziluth.internal.scripts.impl

import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scripts.AbstractScript

class KetherScriptImpl(reader: ScriptReader) : AbstractScript(reader) {

    override fun onEnable() {
        if (reader.isFunction("onEnable")) reader.invoke("onEnable", hashMapOf())
    }

    override fun onLoad() {
        if (reader.isFunction("onLoad")) reader.invoke("onLoad", hashMapOf())
    }

    override fun onDisable() {
        if (reader.isFunction("onDisable")) reader.invoke("onDisable", hashMapOf())
    }

}
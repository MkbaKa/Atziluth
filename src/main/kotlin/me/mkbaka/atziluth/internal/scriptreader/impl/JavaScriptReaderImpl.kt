package me.mkbaka.atziluth.internal.scriptreader.impl

import me.mkbaka.atziluth.internal.hook.nashorn.CompiledScript
import me.mkbaka.atziluth.internal.hook.nashorn.ScriptFactory
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.io.Reader

class JavaScriptReaderImpl : ScriptReader {

    constructor(script: String) : super(script) {
        this.compiledScript = ScriptFactory.compile(script)
    }

    constructor(reader: Reader, script: String = reader.readText()) : super(script) {
        this.compiledScript = ScriptFactory.compile(script)
    }

    private val compiledScript: CompiledScript

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String): T? {
        return compiledScript.scriptEngine[name] as? T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String, def: T): T {
        return compiledScript.scriptEngine[name] as? T ?: def
    }

    override fun eval(sender: CommandSender?, map: Map<String, Any>): Any? {
        val args = map as? HashMap<String, Any> ?: hashMapOf()
        return compiledScript.eval(args.apply { put("sender", sender ?: Bukkit.getConsoleSender()) })
    }

    override fun invoke(name: String, map: Map<String, Any>, vararg args: Any): Any? {
        if (!isFunction(name)) return null
        return compiledScript.invoke(name, map, args)
    }

    override fun isFunction(name: String): Boolean {
        return compiledScript.isFunction(name)
    }

}
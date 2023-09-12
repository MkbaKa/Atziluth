package me.mkbaka.atziluth.internal.scriptreader.impl

import me.mkbaka.atziluth.internal.scriptreader.AbstractScriptReader
import me.mkbaka.atziluth.internal.utils.KetherUtil.eval
import org.bukkit.command.CommandSender
import taboolib.module.configuration.Configuration
import java.io.Reader

class KetherScriptReaderImpl : AbstractScriptReader {

    constructor(script: String) : super(script) {
        initScript(Configuration.loadFromString(script))
    }

    constructor(reader: Reader, script: String = reader.use { it.readText() }) : super(script) {
        initScript(Configuration.loadFromString(script))
    }

    private val topLevels = HashMap<String, Any>()
    private val functions = HashMap<String, String>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String): T {
        return topLevels[name] as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getTopLevel(name: String, def: T): T {
        return topLevels[name] as? T ?: def
    }

    override fun eval(sender: CommandSender?, map: Map<String, Any>): Any? {
        return super.script.eval(sender, map)
    }

    override fun invoke(name: String, map: Map<String, Any>, vararg args: Any): Any? {
        if (!isFunction(name)) return null
        val mapArgs = map["args"] as? HashMap<String, Any> ?: hashMapOf()
        return functions[name]?.eval(map["sender"] as? CommandSender, mapArgs.apply { putAll(topLevels) }) ?: false
    }

    override fun isFunction(name: String): Boolean {
        return functions.containsKey(name)
    }

    private fun initScript(config: Configuration) {
        config.getKeys(false).forEach { node ->

            initVariable<Any>(config, "$node.topLevels") { key, value ->
                topLevels[key] = value
            }

            initVariable<String>(config, "$node.functions") { key, value ->
                functions[key] = value
            }

        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> initVariable(config: Configuration, node: String, callback: (String, T) -> Unit) {
        config.getConfigurationSection(node)?.apply {
            getKeys(false).forEach { key ->
                callback(key, get(key)!! as T)
            }
        }
    }

}
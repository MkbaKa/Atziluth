package me.mkbaka.atziluth.internal.hook.nashorn

import me.mkbaka.atziluth.api.interfaces.Reloadable
import java.util.concurrent.ConcurrentHashMap

object ScriptFactory : Reloadable(priority = 2) {

    private val compiledScripts = ConcurrentHashMap<Int, CompiledScript>()

    /**
     * 预编译脚本
     * @param [script] 脚本
     * @return [CompiledScript]
     */
    fun compile(script: String): CompiledScript {
        return compiledScripts.computeIfAbsent(script.hashCode()) {
            ScriptExpansion(script).apply { scriptEngine.eval(script) }
        }
    }

    override fun reload() {
        compiledScripts.clear()
    }

}
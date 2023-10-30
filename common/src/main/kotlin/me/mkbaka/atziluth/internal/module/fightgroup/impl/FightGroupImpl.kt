package me.mkbaka.atziluth.internal.module.fightgroup.impl

import me.mkbaka.atziluth.internal.module.fightgroup.FightGroup
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.function.console
import taboolib.common5.cdouble

class FightGroupImpl(
    override val scripts: String,
    override val contexts: Map<String, String>
) : FightGroup {

    override fun evalContext(evt: EntityDamageByEntityEvent): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        contexts.forEach { (key, script) ->
            map[key] = AbstractScriptFactory.compile(script)?.evalScript(hashMapOf("event" to evt)) ?: return@forEach console().sendMessage("未找到可用于 $script 的脚本处理器")
        }
        return map
    }

    override fun handle(context: Map<String, Any>): Double {
        return AbstractScriptFactory.compile(scripts)?.let { script ->
            script.evalScript(context).cdouble
        } ?: 0.0
    }

}
package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.bukkit.BukkitAdapter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptCondition
import org.bukkit.entity.LivingEntity

class ScriptCondition(
    mlc: MythicLineConfig,
    private val script: ProxyScriptCondition
) : CustomCondition(mlc) {

    override fun check(ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        return script.castEntityCallback(entity, hashMapOf(
            "abstractEntity" to ae
        ))
    }

    override fun check(al: AbstractLocation): Boolean {
        return script.castLocationCallback(BukkitAdapter.adapt(al), hashMapOf(
            "abstractLocation" to al
        ))
    }

}
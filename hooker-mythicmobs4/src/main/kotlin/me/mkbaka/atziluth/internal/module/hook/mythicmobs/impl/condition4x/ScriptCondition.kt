package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptCondition
import org.bukkit.entity.LivingEntity

class ScriptCondition(
    mlc: MythicLineConfig,
    private val script: ProxyScriptCondition
) : CustomCondition(mlc) {

    override fun check(ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        return script.castEntityCallback(ae, hashMapOf(
            "entity" to entity
        ))
    }

    override fun check(al: AbstractLocation): Boolean {
        return script.castLocationCallback(al, hashMapOf(
            "location" to BukkitAdapter.adapt(al)
        ))
    }

}
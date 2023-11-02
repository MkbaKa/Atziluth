package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
import org.bukkit.entity.LivingEntity

class ScriptMechanic(
    private val mlc: MythicLineConfig,
    private val script: ProxyScriptMechanic
) : CustomMechanic(mlc) {

    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        script.castEntityCallback(caster, entity, hashMapOf(
            "mythicLineConfig" to mlc,
            "caster" to caster,
            "target" to entity,
            "entity" to entity,
            "meta" to meta
        ))
        return true
    }

    override fun castAtLocation(meta: SkillMetadata, al: AbstractLocation): Boolean {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        val location = BukkitAdapter.adapt(al)

        script.castEntityCallback(caster, location, hashMapOf(
            "mythicLineConfig" to mlc,
            "caster" to caster,
            "target" to location,
            "entity" to location,
            "meta" to meta
        ))
        return true
    }

}
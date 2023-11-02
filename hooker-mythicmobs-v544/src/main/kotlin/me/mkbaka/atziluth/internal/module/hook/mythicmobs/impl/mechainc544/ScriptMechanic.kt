package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.bukkit.BukkitAdapter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
import org.bukkit.entity.LivingEntity

class ScriptMechanic(
    private val cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    private val mlc: MythicLineConfig,
    private val script: ProxyScriptMechanic
) : CustomMechanic(cm, mlc) {

    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET
        val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

        script.castEntityCallback(caster, entity, hashMapOf(
            "mythicLineConfig" to mlc,
            "caster" to caster,
            "target" to entity,
            "entity" to entity,
            "meta" to meta
        ))
        return SkillResult.SUCCESS
    }

    override fun castAtLocation(meta: SkillMetadata, al: AbstractLocation): SkillResult {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET
        val location = BukkitAdapter.adapt(al)

        script.castEntityCallback(caster, location, hashMapOf(
            "mythicLineConfig" to mlc,
            "caster" to caster,
            "target" to location,
            "entity" to location,
            "meta" to meta
        ))
        return SkillResult.SUCCESS
    }

}
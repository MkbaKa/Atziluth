package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

class ScriptMechanic(
    private val cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    private val mlc: MythicLineConfig,
    private val script: ProxyScriptMechanic
) : CustomMechanic(cm, mlc) {

    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
        script.castEntityCallback(
            caster, entity, hashMapOf(
                "mythicLineConfig" to mlc,
                "caster" to caster,
                "target" to entity,
                "entity" to entity,
                "meta" to meta
            )
        )
        return SkillResult.SUCCESS
    }

    override fun castToLocation(caster: LivingEntity, loc: Location, meta: SkillMetadata): SkillResult {
        script.castLocationCallback(
            caster, loc, hashMapOf(
                "mythicLineConfig" to mlc,
                "caster" to caster,
                "target" to loc,
                "location" to loc,
                "meta" to meta
            )
        )
        return SkillResult.SUCCESS
    }

}
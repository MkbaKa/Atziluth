package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

class ScriptMechanic(
    private val mlc: MythicLineConfig,
    private val script: ProxyScriptMechanic
) : CustomMechanic(mlc) {

    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): Boolean {
        script.castEntityCallback(
            caster, entity, hashMapOf(
                "mythicLineConfig" to mlc,
                "caster" to caster,
                "target" to entity,
                "entity" to entity,
                "meta" to meta
            )
        )
        return true
    }

    override fun castToLocation(caster: LivingEntity, loc: Location, meta: SkillMetadata): Boolean {
        script.castLocationCallback(
            caster, loc, hashMapOf(
                "mythicLineConfig" to mlc,
                "caster" to caster,
                "target" to loc,
                "location" to loc,
                "meta" to meta
            )
        )
        return true
    }

}
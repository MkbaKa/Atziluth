package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["eval-script", "evalscript"])
class EvalMechanic(
    cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    mlc: MythicLineConfig
) : CustomMechanic(cm, mlc) {

    private val script = mlc.getPlaceholderString(arrayOf("script", "s"), "")

    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
        AbstractScriptFactory.compile(script.get(meta))?.evalScript(parseAllEntries(meta))
        return SkillResult.SUCCESS
    }

}
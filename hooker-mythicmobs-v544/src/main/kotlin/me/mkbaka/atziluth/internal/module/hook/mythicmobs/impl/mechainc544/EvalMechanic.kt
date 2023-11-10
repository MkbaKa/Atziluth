package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["eval-script", "evalscript"])
class EvalMechanic(
    cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    mlc: MythicLineConfig
) : CustomMechanic(cm, mlc) {

    private val script = mlc.getPlaceholderString(arrayOf("script", "s"), "")

    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
        val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

        if (entity.isAlive) {
            AbstractScriptFactory.compile(script.get(meta))?.evalScript(parseAllEntries(meta))
        }
        return SkillResult.SUCCESS
    }
}
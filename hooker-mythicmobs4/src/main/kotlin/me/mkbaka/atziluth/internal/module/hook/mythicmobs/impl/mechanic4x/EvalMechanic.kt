package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["eval-script", "evalscript"])
class EvalMechanic(mlc: MythicLineConfig) : CustomMechanic(mlc) {

    private val script = mlc.getPlaceholderString(arrayOf("script", "s"), "")

    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): Boolean {
        return AbstractScriptFactory.compile(script.get(meta))?.evalScript(parseAllEntries(meta)) != null
    }

}
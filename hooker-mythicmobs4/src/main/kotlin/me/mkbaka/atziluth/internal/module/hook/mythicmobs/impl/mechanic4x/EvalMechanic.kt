package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["eval-script", "evalscript"])
class EvalMechanic(mlc: MythicLineConfig) : CustomMechanic(mlc) {

    private val script = mlc.getPlaceholderString(arrayOf("script", "s"), "")

    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        if (entity.isAlive) {
            AbstractScriptFactory.compile(script.get(meta))?.evalScript(parseAllEntries(meta))
        }
        return true
    }
}
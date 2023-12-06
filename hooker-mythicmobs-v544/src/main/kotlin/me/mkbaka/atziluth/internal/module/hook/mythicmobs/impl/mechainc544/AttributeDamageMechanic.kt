package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["attr-damage", "attrdamage"])
class AttributeDamageMechanic(
    cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    mlc: MythicLineConfig
) : CustomMechanic(cm, mlc) {

    private val basicDamageValue = mlc.getPlaceholderString(arrayOf("damage", "d", "damageValue", "v", "value"), "1.0")
    private val preventKnockback = mlc.getPlaceholderString(arrayOf("preventKnockback", "pkb", "pk"), "false")
    private val ignoreImmunity = mlc.getPlaceholderString(arrayOf("ignoreImmunity", "im", "pi", "ii"), "false")
    private val noDamageTicks = mlc.getPlaceholderString(arrayOf("noDamageTicks", "ndt"), "20")
    private val isClear = mlc.getPlaceholderString(arrayOf("clear", "c", "isClear"), "false")

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
        doAttributeDamageMechanic(
            caster, entity, hashMapOf<String, Any>(
                "damageValue" to basicDamageValue.get(meta),
                "preventKnockback" to preventKnockback.get(meta),
                "ignoreImmunity" to ignoreImmunity.get(meta),
                "noDamageTicks" to noDamageTicks.get(meta),
                "isClear" to isClear.get(meta),
                "meta" to meta
            )
        )
        return SkillResult.SUCCESS
    }
}
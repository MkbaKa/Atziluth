package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageMeta
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.cdouble
import taboolib.common5.cint

@MythicAnnotations.SkillMechanic(["attr-damage", "attrdamage"])
class AttributeDamageMechanic(cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic, mlc: MythicLineConfig) : CustomMechanic(cm, mlc) {

    private val basicDamageValue = mlc.getPlaceholderString(arrayOf("damage", "d", "damageValue", "v", "value"), "1.0")
    private val preventKnockback = mlc.getPlaceholderString(arrayOf("preventKnockback", "pkb", "pk"), "false")
    private val ignoreImmunity = mlc.getPlaceholderString(arrayOf("ignoreImmunity", "im", "pi", "ii"), "false")
    private val noDamageTicks = mlc.getPlaceholderString(arrayOf("noDamageTicks", "ndt"), "20")
    private val isClear = mlc.getPlaceholderString(arrayOf("clear", "c", "isClear"), "false")

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET
        val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

        if (caster.isAlive && entity.isAlive) {
            AtziluthDamageMeta(caster, listOf(entity), AtziluthDamageOptions.new {
                damageValue = this@AttributeDamageMechanic.basicDamageValue.get(meta).cdouble
                preventKnockback = this@AttributeDamageMechanic.preventKnockback.get(meta).cbool
                ignoreImmunity = this@AttributeDamageMechanic.ignoreImmunity.get(meta).cbool
                noDamageTicks = this@AttributeDamageMechanic.noDamageTicks.get(meta).cint
                isClear = this@AttributeDamageMechanic.isClear.get(meta).cbool
                setAttributes(parseToAttribute(meta))
            }).doDamage()
        }

        return SkillResult.SUCCESS
    }

}
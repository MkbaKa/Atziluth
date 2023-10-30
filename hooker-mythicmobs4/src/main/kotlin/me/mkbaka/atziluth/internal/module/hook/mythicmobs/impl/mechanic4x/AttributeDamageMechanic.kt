package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageMeta
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.cdouble
import taboolib.common5.cint
import java.util.*

@MythicAnnotations.SkillMechanic(["attr-damage", "attrdamage"])
class AttributeDamageMechanic(mlc: MythicLineConfig) : CustomMechanic(mlc) {

    private val basicDamageValue = mlc.getPlaceholderString(arrayOf("damage", "d", "damageValue", "v", "value"), "1.0")
    private val preventKnockback = mlc.getPlaceholderString(arrayOf("preventKnockback", "pk"), "false")
    private val ignoreImmunity = mlc.getPlaceholderString(arrayOf("ignoreImmunity", "im", "pi", "ii"), "false")
    private val noDamageTicks = mlc.getPlaceholderString(arrayOf("noDamageTicks", "ndt"), "-1")
    private val isClear = mlc.getPlaceholderString(arrayOf("clear", "c", "isClear"), "false")

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        if (caster.isAlive && entity.isAlive) {
            val tempAttributeData = TempAttributeData.new(caster.uniqueId, UUID.randomUUID().toString(), parseToAttribute(meta))
            AtziluthDamageMeta(caster, listOf(entity), AtziluthDamageOptions(
                basicDamageValue.get(meta).cdouble,
                preventKnockback.get(meta).cbool,
                ignoreImmunity.get(meta).cbool,
                noDamageTicks.get(meta).cint,
                tempAttributeData,
                isClear.get(meta).cbool
            )).doDamage()
        }

        return true
    }

}
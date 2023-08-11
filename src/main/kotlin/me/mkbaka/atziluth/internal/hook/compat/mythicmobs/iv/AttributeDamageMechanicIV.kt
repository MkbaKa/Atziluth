package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.internal.utils.EntityUtil
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.cdouble

class AttributeDamageMechanicIV(
    mlc: MythicLineConfig
) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill {

    private val damageValue = mlc.getPlaceholderString(arrayOf("damage", "d"), "1.0")
    private val isClear = mlc.getPlaceholderString(arrayOf("isClear", "clear", "c"), "false")
    private val entries = mlc.entrySet()

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        if (entity.isAlive && caster.isAlive) {
            val attrs = entries.map { PlaceholderString.of("${it.key}: ${it.value}").get(meta) }
            val entities = meta.entityTargets.filter { it.bukkitEntity is LivingEntity }.map { it.bukkitEntity as LivingEntity }
//            if (caster is Player) TempDataManager.getPlayerData(caster.uniqueId)!!.saveData("MythicMobs-AttrDamageData", attrs)
            EntityUtil.doAttributeDamage(caster, entities, damageValue.get(meta).cdouble, attrs, isClear = isClear.get(meta).cbool)
//            EntityUtil.doAttributeDamage(caster, entity, damageValue.get(meta).cdouble, attrs, isClear = isClear.get(meta).cbool)
        }
        return true
    }

}
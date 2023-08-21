package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.internal.data.TempDataManager
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.utils.EntityUtil
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common5.cbool
import taboolib.common5.cdouble

@CustomSkillMechanic(["attr-damage", "attrdamage"], MythicMobVersion.IV)
class AttributeDamageMechanicIV(
    mlc: MythicLineConfig
) : CustomSkillMechanicIV(mlc) {

    private val damageValue = mlc.getPlaceholderString(arrayOf("damage", "d"), "1.0")
    private val isClear = mlc.getPlaceholderString(arrayOf("isClear", "clear", "c"), "false")
    private val entries = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        if (entity.isAlive && caster.isAlive) {
            val attrs = entries.map { PlaceholderString.of("${it.key}: ${it.value}").get(meta) }
            val entities = meta.entityTargets.filter { it.bukkitEntity is LivingEntity }.map { it.bukkitEntity as LivingEntity }
            if (caster is Player) TempDataManager.getPlayerData(caster.uniqueId)!!
                .saveData("MythicMobs-AttrDamageData", entries)
            EntityUtil.doAttributeDamage(caster, entities, damageValue.get(meta).cdouble, attrs, isClear = isClear.get(meta).cbool)
        }
        return true
    }

}
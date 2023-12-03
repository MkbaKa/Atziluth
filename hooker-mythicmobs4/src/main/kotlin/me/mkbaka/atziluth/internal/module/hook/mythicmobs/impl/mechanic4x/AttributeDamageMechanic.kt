package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageMeta
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import me.mkbaka.atziluth.utils.EntityUtil.removeMetadataEZ
import me.mkbaka.atziluth.utils.EntityUtil.setMetadataEZ
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.cdouble
import taboolib.common5.cint

@MythicAnnotations.SkillMechanic(["attr-damage", "attrdamage"])
class AttributeDamageMechanic(mlc: MythicLineConfig) : CustomMechanic(mlc) {

    private val basicDamageValue = mlc.getPlaceholderString(arrayOf("damage", "d", "damageValue", "v", "value"), "1.0")
    private val preventKnockback = mlc.getPlaceholderString(arrayOf("preventKnockback", "pkb", "pk"), "false")
    private val ignoreImmunity = mlc.getPlaceholderString(arrayOf("ignoreImmunity", "im", "pi", "ii"), "false")
    private val noDamageTicks = mlc.getPlaceholderString(arrayOf("noDamageTicks", "ndt"), "20")
    private val isClear = mlc.getPlaceholderString(arrayOf("clear", "c", "isClear"), "false")

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        if (caster.hasMetadata(AbstractMythicMobsHooker.attrDamage_metadata)) return false

        if (caster.isAlive && entity.isAlive) {
            meta.caster.isUsingDamageSkill = true
            caster.setMetadataEZ(AbstractMythicMobsHooker.attrDamage_metadata, true)
            Atziluth.tempDataManager.getData(caster.uniqueId)?.saveData(AbstractMythicMobsHooker.attrDamage_tempdata, parseAllEntries(meta))
            AtziluthDamageMeta(caster, listOf(entity), AtziluthDamageOptions.new {
                damageValue = this@AttributeDamageMechanic.basicDamageValue.get(meta).cdouble
                preventKnockback = this@AttributeDamageMechanic.preventKnockback.get(meta).cbool
                ignoreImmunity = this@AttributeDamageMechanic.ignoreImmunity.get(meta).cbool
                noDamageTicks = this@AttributeDamageMechanic.noDamageTicks.get(meta).cint
                isClear = this@AttributeDamageMechanic.isClear.get(meta).cbool
                setAttributes(parseToAttribute(meta))
            }).doDamage()
            Atziluth.tempDataManager.getData(caster.uniqueId)?.removeData(AbstractMythicMobsHooker.attrDamage_tempdata)
            caster.removeMetadataEZ(AbstractMythicMobsHooker.attrDamage_metadata)
            meta.caster.isUsingDamageSkill = false
        }

        return true
    }

}
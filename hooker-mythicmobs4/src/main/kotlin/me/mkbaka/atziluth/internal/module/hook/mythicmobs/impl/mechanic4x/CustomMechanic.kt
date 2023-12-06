package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageMeta
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import me.mkbaka.atziluth.utils.EntityUtil.removeMetadataEZ
import me.mkbaka.atziluth.utils.EntityUtil.setMetadataEZ
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.cdouble
import taboolib.common5.cint

abstract class CustomMechanic(private val mlc: MythicLineConfig) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill,
    ITargetedLocationSkill {

    final override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        return if (caster.isAlive && entity.isAlive) castToEntity(caster, entity, meta) else false
    }

    final override fun castAtLocation(meta: SkillMetadata, al: AbstractLocation): Boolean {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false

        return if (caster.isAlive) castToLocation(caster, BukkitAdapter.adapt(al), meta) else false
    }

    open fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): Boolean {
        return true
    }

    open fun castToLocation(caster: LivingEntity, loc: Location, meta: SkillMetadata): Boolean {
        return true
    }

    fun doAttributeDamageMechanic(caster: LivingEntity, entity: LivingEntity, args: Map<String, Any>) {
        if (caster.hasMetadata(AbstractMythicMobsHooker.attrDamage_metadata)) return

        val meta = args["meta"] as SkillMetadata

        meta.caster.isUsingDamageSkill = true

        caster.setMetadataEZ(AbstractMythicMobsHooker.attrDamage_metadata, true)
        Atziluth.tempDataManager.getData(caster.uniqueId)
            ?.saveData(AbstractMythicMobsHooker.attrDamage_tempdata, parseAllEntries(meta).apply {
                this["caster"] = caster
                this["entity"] = entity
            })

        AtziluthDamageMeta(caster, listOf(entity), AtziluthDamageOptions.new {
            damageValue = args.getOrDefault("damageValue", 1.0).cdouble
            preventKnockback = args.getOrDefault("preventKnockback", false).cbool
            ignoreImmunity = args.getOrDefault("ignoreImmunity", false).cbool
            noDamageTicks = args.getOrDefault("noDamageTicks", 20).cint
            isClear = args.getOrDefault("isClear", false).cbool
            setAttributes(parseToAttribute(meta))
        }).doDamage()

        Atziluth.tempDataManager.getData(caster.uniqueId)?.removeData(AbstractMythicMobsHooker.attrDamage_tempdata)
        caster.removeMetadataEZ(AbstractMythicMobsHooker.attrDamage_metadata)

        meta.caster.isUsingDamageSkill = false
    }

    fun parseAllEntries(meta: SkillMetadata): MutableMap<String, Any> {
        val map = hashMapOf<String, Any>()
        mlc.entrySet().forEach { entry ->
            map[entry.key.parse(meta)] = entry.value.parse(meta)
        }
        return map
    }

    fun parseToAttribute(meta: SkillMetadata): MutableMap<String, DoubleArray> {
        val map = hashMapOf<String, DoubleArray>()
        mlc.entrySet().forEach { entry ->
            val matcher = Atziluth.number_pattern.matcher(entry.value.parse(meta))
            val array = arrayListOf<Double>()
            while (matcher.find()) {
                array += matcher.group(0).cdouble
            }
            map[entry.key.parse(meta)] = array.toDoubleArray()
        }
        return map
    }

    private fun String.parse(meta: SkillMetadata): String {
        return PlaceholderString.of(this).get(meta)
    }

}
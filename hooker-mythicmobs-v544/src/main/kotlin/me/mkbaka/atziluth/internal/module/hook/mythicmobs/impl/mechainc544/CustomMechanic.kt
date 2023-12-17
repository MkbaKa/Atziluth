package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.ITargetedLocationSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageMeta
import me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.utils.EntityUtil.removeMetadataEZ
import me.mkbaka.atziluth.utils.EntityUtil.setMetadataEZ
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.cdouble
import taboolib.common5.cint

abstract class CustomMechanic(
    private val cm: CustomMechanic,
    private val mlc: MythicLineConfig
) : SkillMechanic(cm.manager, cm.file, mlc.line, mlc), ITargetedEntitySkill, ITargetedLocationSkill {

    final override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET
        val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET
        return castToEntity(caster, entity, meta)
    }

    final override fun castAtLocation(meta: SkillMetadata, al: AbstractLocation): SkillResult {
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET
        return castToLocation(caster, BukkitAdapter.adapt(al), meta)
    }

    open fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
        return SkillResult.SUCCESS
    }

    open fun castToLocation(caster: LivingEntity, loc: Location, meta: SkillMetadata): SkillResult {
        return SkillResult.SUCCESS
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
            setAttributes(parseToMap(meta))
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

    fun parseToList(meta: SkillMetadata): MutableList<String> {
        val list = mutableListOf<String>()
        mlc.entrySet().forEach { (key, value) ->
            list.add("${key.parse(meta)}: ${value.parse(meta)}")
        }
        return list
    }

    fun parseToMap(meta: SkillMetadata): MutableMap<String, DoubleArray> {
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
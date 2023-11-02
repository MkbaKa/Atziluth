package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.ITargetedLocationSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import taboolib.common5.cdouble

abstract class CustomMechanic(
    private val cm: CustomMechanic,
    private val mlc: MythicLineConfig
): SkillMechanic(cm.manager, cm.file, mlc.line, mlc), ITargetedEntitySkill, ITargetedLocationSkill {

    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
        return SkillResult.SUCCESS
    }

    override fun castAtLocation(meta: SkillMetadata, al: AbstractLocation): SkillResult {
        return SkillResult.SUCCESS
    }

    fun parseAllEntries(meta: SkillMetadata): Map<String, Any> {
        val map = hashMapOf<String, Any>()
        mlc.entrySet().forEach { entry ->
            map[entry.key.parse(meta)] = entry.value.parse(meta)
        }
        return map
    }

    fun parseToAttribute(meta: SkillMetadata): HashMap<String, Array<Double>> {
        val map = hashMapOf<String, Array<Double>>()
        mlc.entrySet().forEach { entry ->
            val str = entry.value.parse(meta)
            val mather = AbstractMythicMobsHooker.number_pattern.matcher(str)
            if (mather.find()) {
                map[entry.key.parse(meta)] = arrayOf(mather.group(1).cdouble, mather.group(4).cdouble)
            } else {
                map[entry.key.parse(meta)] = arrayOf(str.cdouble, str.cdouble)
            }
        }
        return map
    }

    private fun String.parse(meta: SkillMetadata): String {
        return PlaceholderString.of(this).get(meta)
    }

}
package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.Atziluth
import taboolib.common5.cdouble

abstract class CustomMechanic(private val mlc: MythicLineConfig) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill,
    ITargetedLocationSkill {

    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        return true
    }

    override fun castAtLocation(meta: SkillMetadata, al: AbstractLocation): Boolean {
        return true
    }

    fun parseAllEntries(meta: SkillMetadata): Map<String, Any> {
        val map = hashMapOf<String, Any>()
        mlc.entrySet().forEach { entry ->
            map[entry.key.parse(meta)] = entry.value.parse(meta)
        }
        return map
    }

    fun parseToAttribute(meta: SkillMetadata): HashMap<String, DoubleArray> {
        val map = hashMapOf<String, DoubleArray>()
        mlc.entrySet().forEach { entry ->
            val str = entry.value.parse(meta)
            val mather = Atziluth.number_pattern.matcher(str)
            if (mather.find()) {
                map[entry.key.parse(meta)] = doubleArrayOf(mather.group(1).cdouble, mather.group(4).cdouble)
            } else {
                map[entry.key.parse(meta)] = doubleArrayOf(str.cdouble, str.cdouble)
            }
        }
        return map
    }

    private fun String.parse(meta: SkillMetadata): String {
        return PlaceholderString.of(this).get(meta)
    }

}
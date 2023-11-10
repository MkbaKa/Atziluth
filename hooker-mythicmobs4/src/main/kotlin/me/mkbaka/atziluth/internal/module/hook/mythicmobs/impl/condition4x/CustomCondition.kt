package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillCondition
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition

abstract class CustomCondition(
    val mlc: MythicLineConfig
) : SkillCondition(mlc.line), IEntityCondition, ILocationCondition {

    override fun check(ae: AbstractEntity): Boolean {
        return true
    }

    override fun check(al: AbstractLocation): Boolean {
        return true
    }

    fun parseAllEntries(): Map<String, Any> {
        val map = hashMapOf<String, Any>()
        mlc.entrySet().forEach { entry ->
            map[entry.key] = entry.value
        }
        return map
    }

}
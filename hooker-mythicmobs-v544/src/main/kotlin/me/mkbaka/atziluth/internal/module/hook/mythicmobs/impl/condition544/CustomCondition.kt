package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.conditions.IEntityCondition
import io.lumine.mythic.api.skills.conditions.ILocationCondition
import io.lumine.mythic.core.skills.SkillCondition

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
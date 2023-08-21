package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import java.util.concurrent.ConcurrentHashMap

abstract class CustomSkillMechanicIV(
    mlc: MythicLineConfig
) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill {

    companion object {

        val skills = ConcurrentHashMap<String, Class<CustomSkillMechanicIV>>()

    }

}
package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillCondition
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition

abstract class CustomSkillConditionIV(
    mlc: MythicLineConfig
) : SkillCondition(mlc.line), IEntityCondition
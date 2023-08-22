package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.conditions.IEntityCondition
import io.lumine.mythic.core.skills.SkillCondition


abstract class CustomSkillConditionV(
    mlc: MythicLineConfig
) : SkillCondition(mlc.line), IEntityCondition
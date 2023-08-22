package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.mechanics.CustomMechanic

abstract class CustomSkillMechanicV(
    cm: CustomMechanic,
    mlc: MythicLineConfig
) : SkillMechanic(cm.manager, cm.file, mlc.line, mlc), ITargetedEntitySkill
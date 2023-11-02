package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.core.skills.targeters.CustomTargeter
import io.lumine.mythic.core.skills.targeters.IEntitySelector

abstract class CustomEntityTargeter(
    ct: CustomTargeter,
    mlc: MythicLineConfig
) : IEntitySelector(ct.manager, mlc) {

    override fun getEntities(meta: SkillMetadata): HashSet<AbstractEntity> {
        return hashSetOf()
    }

}
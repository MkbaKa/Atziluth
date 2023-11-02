package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector

abstract class CustomEntityTargeter(
    mlc: MythicLineConfig
) : IEntitySelector(mlc) {

    override fun getEntities(meta: SkillMetadata): HashSet<AbstractEntity> {
        return hashSetOf()
    }

}
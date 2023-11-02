package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter544

import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.core.skills.targeters.CustomTargeter
import io.lumine.mythic.core.skills.targeters.ILocationSelector

abstract class CustomLocationTargeter(
    ct: CustomTargeter,
    mlc: MythicLineConfig
) : ILocationSelector(ct.manager, mlc) {

    override fun getLocations(meta: SkillMetadata): HashSet<AbstractLocation> {
        return hashSetOf()
    }

}
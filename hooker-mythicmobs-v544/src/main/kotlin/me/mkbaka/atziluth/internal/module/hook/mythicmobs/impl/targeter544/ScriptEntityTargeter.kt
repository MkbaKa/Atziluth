package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.skills.targeters.CustomTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyEntityTargeter

class ScriptEntityTargeter(
    ct: CustomTargeter,
    mlc: MythicLineConfig,
    private val script: ProxyEntityTargeter
) : CustomEntityTargeter(ct, mlc) {

    override fun getEntities(meta: SkillMetadata): HashSet<AbstractEntity> {
        meta.caster.entity.bukkitEntity.type
        return script.castEntityCallback(hashMapOf(
            "meta" to meta
        )).map { BukkitAdapter.adapt(it) }.toHashSet()
    }

}
package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyEntityTargeter

class ScriptEntityTargeter(
    mlc: MythicLineConfig,
    private val script: ProxyEntityTargeter
) : CustomEntityTargeter(mlc) {

    override fun getEntities(meta: SkillMetadata): HashSet<AbstractEntity> {
        return script.castEntityCallback(hashMapOf(
            "meta" to meta
        )).map { BukkitAdapter.adapt(it) }.toHashSet()
    }

}
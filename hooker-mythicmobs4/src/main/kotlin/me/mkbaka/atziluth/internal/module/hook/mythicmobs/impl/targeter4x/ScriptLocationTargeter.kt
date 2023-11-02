package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter

class ScriptLocationTargeter(
    mlc: MythicLineConfig,
    private val script: ProxyLocationTargeter
) : CustomLocationTargeter(mlc) {

    override fun getLocations(meta: SkillMetadata): HashSet<AbstractLocation> {
        return script.getLocationCallback(hashMapOf(
            "meta" to meta
        )).map { BukkitAdapter.adapt(it) }.toHashSet()
    }

}
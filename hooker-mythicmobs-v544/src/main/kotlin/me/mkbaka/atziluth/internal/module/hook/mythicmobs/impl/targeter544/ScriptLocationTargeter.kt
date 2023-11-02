package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter544

import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.skills.targeters.CustomTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter

class ScriptLocationTargeter(
    ct: CustomTargeter,
    mlc: MythicLineConfig,
    private val script: ProxyLocationTargeter
) : CustomLocationTargeter(ct, mlc) {

    override fun getLocations(meta: SkillMetadata): HashSet<AbstractLocation> {
        return script.getLocationCallback(hashMapOf(
            "meta" to meta
        )).map { BukkitAdapter.adapt(it) }.toHashSet()
    }

}
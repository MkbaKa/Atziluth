package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition544

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.bukkit.BukkitAdapter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool

@MythicAnnotations.SkillCondition(["eval-script", "evalscript"])
class EvalCondition(mlc: MythicLineConfig) : CustomCondition(mlc) {

    private val script = mlc.getString(arrayOf("script", "s"), "")

    override fun check(ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        return AbstractScriptFactory.compile(script)?.evalScript(parseAllEntries().also {
            it["entity"] to entity
        }).cbool
    }

    override fun check(al: AbstractLocation): Boolean {
        val location = BukkitAdapter.adapt(al)
        return AbstractScriptFactory.compile(script)?.evalScript(parseAllEntries().also {
            it["location"] to location
        }).cbool
    }

}
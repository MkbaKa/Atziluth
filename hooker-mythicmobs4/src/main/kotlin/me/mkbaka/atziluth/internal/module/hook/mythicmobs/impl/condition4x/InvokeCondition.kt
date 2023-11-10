package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import me.mkbaka.atziluth.internal.configuration.impl.ScriptsComponent
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool

@MythicAnnotations.SkillCondition(["invoke-script", "invokescript"])
class InvokeCondition(mlc: MythicLineConfig) : CustomCondition(mlc) {

    private val script = mlc.getString(arrayOf("script", "s"), "")
    private val func = mlc.getString(arrayOf("func", "function"), "")

    override fun check(ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        return ScriptsComponent.scripts[script]?.invokeFunction(func, parseAllEntries().also {
            it["entity"] to entity
        }).cbool
    }

    override fun check(al: AbstractLocation): Boolean {
        val location = BukkitAdapter.adapt(al)
        return ScriptsComponent.scripts[script]?.invokeFunction(func, parseAllEntries().also {
            it["location"] to location
        }).cbool
    }

}
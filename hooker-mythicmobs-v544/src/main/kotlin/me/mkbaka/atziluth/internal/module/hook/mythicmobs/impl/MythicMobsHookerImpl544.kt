package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.skills.SkillCondition
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.placeholders.Placeholder
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.registerBukkitListener
import taboolib.library.reflex.Reflex.Companion.invokeConstructor

class MythicMobsHookerImpl544 : AbstractMythicMobsHooker() {

    override val mechanicPackage: String
        get() = "mechanic5x"

    override val conditionPackage: String
        get() = ""

    override val mechanicLoadEvent: Class<MythicMechanicLoadEvent>
        get() = MythicMechanicLoadEvent::class.java

    override val conditionLoadEvent: Class<MythicConditionLoadEvent>
        get() = MythicConditionLoadEvent::class.java

    override val reloadEvent: Class<MythicReloadedEvent>
        get() = MythicReloadedEvent::class.java

    override fun onReload() {
        registerPlaceholder()
    }

    override fun registerPlaceholder() {
        MythicBukkit.inst().placeholderManager.apply {
            register("caster.attr", Placeholder.meta { meta, args ->
                return@meta handlePlaceholder(meta.caster.entity.bukkitEntity as LivingEntity, args)
            })
            register("entity.attr", Placeholder.entity { ae, args ->
                return@entity handlePlaceholder(ae.bukkitEntity, args)
            })
            register("target.attr", Placeholder.target { _, target, args ->
                return@target handlePlaceholder(target.bukkitEntity as LivingEntity, args)
            })
            register("parent.attr", Placeholder.parent { parent, args ->
                return@parent handlePlaceholder(parent.bukkitEntity, args)
            })
        }
    }

    override fun registerMechanicListener() {
        registerBukkitListener(mechanicLoadEvent) { event ->
            skillClasses[event.mechanicName.lowercase()]?.let { mechanic ->
                event.register(mechanic.invokeConstructor(event.container, event.config) as SkillMechanic)
            }
        }
    }

    override fun registerConditionListener() {
        registerBukkitListener(conditionLoadEvent) { event ->
            conditionClasses[event.conditionName.lowercase()]?.let { condition ->
                event.register(condition.invokeConstructor(event.container, event.config) as SkillCondition)
            }
        }
    }

}
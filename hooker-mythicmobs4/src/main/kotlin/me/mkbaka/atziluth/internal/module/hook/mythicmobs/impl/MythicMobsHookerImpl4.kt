package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent
import io.lumine.xikage.mythicmobs.skills.SkillCondition
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import taboolib.common.platform.function.registerBukkitListener
import taboolib.library.reflex.Reflex.Companion.invokeConstructor

abstract class MythicMobsHookerImpl4 : AbstractMythicMobsHooker() {

    override val mechanicPackage: String
        get() = "mechanic4x"

    override val conditionPackage: String
        get() = ""

    override val mechanicLoadEvent: Class<MythicMechanicLoadEvent>
        get() = MythicMechanicLoadEvent::class.java

    override val conditionLoadEvent: Class<MythicConditionLoadEvent>
        get() = MythicConditionLoadEvent::class.java

    override val reloadEvent: Class<MythicReloadedEvent>
        get() = MythicReloadedEvent::class.java

    override fun registerMechanicListener() {
        registerBukkitListener(mechanicLoadEvent) { event ->
            skillClasses[event.mechanicName.lowercase()]?.let { mechanic ->
                event.register(mechanic.invokeConstructor(event.config) as SkillMechanic)
            }
        }
    }

    override fun registerConditionListener() {
        registerBukkitListener(conditionLoadEvent) { event ->
            conditionClasses[event.conditionName.lowercase()]?.let { condition ->
                event.register(condition.invokeConstructor(event.config) as SkillCondition)
            }
        }
    }

}
package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent
import io.lumine.xikage.mythicmobs.skills.*
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition4x.ScriptCondition
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x.ScriptMechanic
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x.ScriptEntityTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x.ScriptLocationTargeter
import taboolib.common.platform.function.registerBukkitListener
import taboolib.library.reflex.Reflex.Companion.invokeConstructor

abstract class MythicMobsHookerImpl4 : AbstractMythicMobsHooker() {

    init {
        registerBukkitListener(TriggeredSkill::class.java) { event ->
            if (event.data.cause == SkillTrigger.ATTACK && event.data.caster.isUsingDamageSkill) {
                event.setCancelled()
            }
        }
    }

    override val instance: MythicMobs
        get() = MythicMobs.inst()

    override val reloadEvent: Class<MythicReloadedEvent>
        get() = MythicReloadedEvent::class.java

    override fun registerMechanicListener() {
        registerBukkitListener(MythicMechanicLoadEvent::class.java) { event ->
            val mechanicName = event.mechanicName.lowercase()

            skillClasses[mechanicName]?.let { mechanic ->
                event.register(mechanic.invokeConstructor(event.config) as SkillMechanic)
                return@registerBukkitListener
            }

            scriptMechanics[mechanicName]?.let { scriptMechanic ->
                event.register(ScriptMechanic(event.config, scriptMechanic))
            }
        }
    }

    override fun registerConditionListener() {
        registerBukkitListener(MythicConditionLoadEvent::class.java) { event ->
            val conditionName = event.conditionName.lowercase()

            conditionClasses[conditionName]?.let { condition ->
                event.register(condition.invokeConstructor(event.config) as SkillCondition)
                return@registerBukkitListener
            }

            scriptConditions[conditionName]?.let { scriptCondition ->
                event.register(ScriptCondition(event.config, scriptCondition))
            }
        }
    }

    override fun registerTargetListener() {
        registerBukkitListener(MythicTargeterLoadEvent::class.java) { event ->
            val targeterName = event.targeterName.lowercase()

            targetClasses[targeterName]?.let { target ->
                event.register(target.invokeConstructor(event.config) as SkillTargeter)
                return@registerBukkitListener
            }

            scriptEntityTargeters[targeterName]?.let { entityTargeter ->
                event.register(ScriptEntityTargeter(event.config, entityTargeter))
                return@registerBukkitListener
            }

            scriptLocationTargeters[targeterName]?.let { locationTargeter ->
                event.register(ScriptLocationTargeter(event.config, locationTargeter))
            }
        }
    }

}
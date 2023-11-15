package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.mythic.api.skills.targeters.ISkillTargeter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent
import io.lumine.mythic.core.skills.SkillCondition
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.placeholders.Placeholder
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.condition544.ScriptCondition
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544.ScriptMechanic
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter544.ScriptEntityTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter544.ScriptLocationTargeter
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.registerBukkitListener
import taboolib.library.reflex.Reflex.Companion.invokeConstructor

class MythicMobsHookerImpl544 : AbstractMythicMobsHooker() {

    override val instance: MythicBukkit
        get() = MythicBukkit.inst()

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
            register("trigger.attr", Placeholder.meta { meta, args ->
                return@meta handlePlaceholder(meta.trigger.bukkitEntity as LivingEntity, args)
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
        registerBukkitListener(MythicMechanicLoadEvent::class.java) { event ->
            val mechanicName = event.mechanicName.lowercase()

            skillClasses[event.mechanicName.lowercase()]?.let { mechanic ->
                event.register(mechanic.invokeConstructor(event.container, event.config) as SkillMechanic)
                return@registerBukkitListener
            }

            scriptMechanics[mechanicName]?.let { scriptMechanic ->
                event.register(ScriptMechanic(event.container, event.config, scriptMechanic))
            }
        }
    }

    override fun registerConditionListener() {
        registerBukkitListener(MythicConditionLoadEvent::class.java) { event ->
            val conditionName = event.conditionName.lowercase()

            conditionClasses[conditionName]?.let { condition ->
                event.register(condition.invokeConstructor(event.container, event.config) as SkillCondition)
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
                event.register(target.invokeConstructor(event.container, event.config) as ISkillTargeter)
                return@registerBukkitListener
            }

            scriptEntityTargeters[targeterName]?.let { entityTargeter ->
                event.register(ScriptEntityTargeter(event.container, event.config, entityTargeter))
                return@registerBukkitListener
            }

            scriptLocationTargeters[targeterName]?.let { locationTargeter ->
                event.register(ScriptLocationTargeter(event.container, event.config, locationTargeter))
            }
        }
    }

}
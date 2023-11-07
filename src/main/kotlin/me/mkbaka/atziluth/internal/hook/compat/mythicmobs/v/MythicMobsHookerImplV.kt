package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v

import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.skills.placeholders.Placeholder
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import org.bukkit.entity.LivingEntity
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import java.util.concurrent.ConcurrentHashMap

class MythicMobsHookerImplV(val inst: MythicBukkit) :
    AbstractMythicMobsHooker<MythicMechanicLoadEvent, MythicConditionLoadEvent, MythicReloadedEvent>() {

    override val mechanicEventClass: Class<MythicMechanicLoadEvent>
        get() = MythicMechanicLoadEvent::class.java

    override val conditionEventClass: Class<MythicConditionLoadEvent>
        get() = MythicConditionLoadEvent::class.java

    override val reloadEvent: Class<MythicReloadedEvent>
        get() = MythicReloadedEvent::class.java

    override fun registerSkill(e: MythicMechanicLoadEvent) {
        skills[e.mechanicName.lowercase()]?.let { e.register(it.invokeConstructor(e.container, e.config)) }
    }

    override fun registerCondition(e: MythicConditionLoadEvent) {
        conditions[e.conditionName.lowercase()]?.let { e.register(it.invokeConstructor(e.config)) }
    }

    override fun registerPlaceholder() {
        inst.placeholderManager.apply {
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

    companion object {

        val skills = ConcurrentHashMap<String, Class<CustomSkillMechanicV>>()
        val conditions = ConcurrentHashMap<String, Class<CustomSkillConditionV>>()

        fun parse(entries: Set<Map.Entry<String, String>>, meta: SkillMetadata): List<String> {
            return entries.map {
                "${it.key.toMythicValue(meta)}: ${it.value.toMythicValue(meta)}"
            }
        }

        private fun String.toMythicValue(meta: SkillMetadata): String {
            return PlaceholderString.of(this).get(meta)
        }


    }

}
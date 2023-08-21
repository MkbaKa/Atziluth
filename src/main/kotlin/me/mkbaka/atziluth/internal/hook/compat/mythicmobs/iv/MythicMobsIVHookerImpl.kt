package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import org.bukkit.entity.LivingEntity
import taboolib.library.reflex.Reflex.Companion.invokeConstructor

class MythicMobsIVHookerImpl(private val inst: MythicMobs) :
    AbstractMythicMobsHooker<MythicMechanicLoadEvent, MythicReloadedEvent>() {

    override val mechanicEventClass: Class<MythicMechanicLoadEvent>
        get() = MythicMechanicLoadEvent::class.java

    override val reloadEvent: Class<MythicReloadedEvent>
        get() = MythicReloadedEvent::class.java

    override fun registerSkill(e: MythicMechanicLoadEvent) {
        CustomSkillMechanicIV.skills[e.mechanicName.lowercase()]?.let { e.register(it.invokeConstructor(e.config)) }
    }

    override fun registerPlaceholder() {
        inst.placeholderManager.apply {
            register("caster.attr", Placeholder.meta { meta, args ->
                return@meta handlePlaceholder(meta.caster.entity.bukkitEntity as LivingEntity, args)
            })
            register("entity.attr", Placeholder.entity { ae, args ->
                return@entity handlePlaceholder(ae.bukkitEntity, args)
            })

            if (MythicMobVersion.subVersion > 7) {
                register("target.attr", Placeholder.target { _, target, args ->
                    return@target handlePlaceholder(target.bukkitEntity as LivingEntity, args)
                })
                register("parent.attr", Placeholder.parent { parent, args ->
                    return@parent handlePlaceholder(parent.bukkitEntity, args)
                })
            }

        }
    }

}
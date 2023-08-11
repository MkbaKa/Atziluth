package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import org.bukkit.entity.LivingEntity

class MythicMobsIVHookerImpl(private val inst: MythicMobs) : AbstractMythicMobsHooker<MythicMechanicLoadEvent, MythicReloadedEvent>() {

    override val mechanicEventClass: Class<MythicMechanicLoadEvent>
        get() = MythicMechanicLoadEvent::class.java

    override val reloadEvent: Class<MythicReloadedEvent>
        get() = MythicReloadedEvent::class.java

    override fun registerSkill(e: MythicMechanicLoadEvent) {
        when (e.mechanicName.lowercase()) {
            "add-attr", "addattr" -> e.register(AttributeMechanicIV.Add(e.config))
            "take-attr", "takeattr" -> e.register(AttributeMechanicIV.Take(e.config))
            "attr-damage", "attrdamage" -> e.register(AttributeDamageMechanicIV(e.config))
        }
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
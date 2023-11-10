package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder
import org.bukkit.entity.LivingEntity

class MythicMobsHookerImpl411 : MythicMobsHookerImpl4() {

    override fun onReload() {
        registerPlaceholder()
    }
    
    override fun registerPlaceholder() {
        MythicMobs.inst().placeholderManager.apply {
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

}
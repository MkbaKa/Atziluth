package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder
import org.bukkit.entity.LivingEntity

class MythicMobsHookerImpl472 : MythicMobsHookerImpl4() {

    init {
        registerPlaceholder()
    }

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
        }
    }

}
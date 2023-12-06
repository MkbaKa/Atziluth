package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyPlaceholder
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
            register("caster.tempdata", Placeholder.meta { meta, str ->
                val entity = meta.caster.entity.bukkitEntity as? LivingEntity ?: return@meta "Not LivingEntity"
                val args = str.split(":")
                val key = args.getOrElse(0) { return@meta ("undefined") }
                return@meta Atziluth.tempDataManager.getData(entity.uniqueId)?.getData(key)?.toString()
                    ?: "undefined data $key"
            })
            scriptPlaceholders.forEach { (placeholder, proxy) ->
                register(placeholder, Placeholder.meta { meta, args ->
                    return@meta proxy.callback(
                        hashMapOf(
                            "meta" to meta,
                            "args" to args
                        )
                    )
                })
            }
        }
    }

    override fun registerPlaceholder(placeholder: ProxyPlaceholder) {
        placeholder.names.forEach { name ->
            MythicMobs.inst().placeholderManager.register(name, Placeholder.meta { meta, args ->
                return@meta placeholder.callback(hashMapOf(
                    "meta" to meta,
                    "args" to args
                ))
            })
        }
    }

}
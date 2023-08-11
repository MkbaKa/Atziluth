package me.mkbaka.atziluth.internal.hook.compat.mythicmobs

import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.xikage.mythicmobs.MythicMobs
import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.bridge.AttributeValueType
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.MythicMobsIVHookerImpl
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.MythicMobsVHookerImpl
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common5.format

abstract class AbstractMythicMobsHooker<E, R> {

    abstract val mechanicEventClass: Class<E>
    abstract val reloadEvent: Class<R>

    init {
        registerBukkitListener(mechanicEventClass) { event ->
            registerSkill(event)
        }

        registerBukkitListener(reloadEvent) { _ ->
            registerPlaceholder()
        }
    }

    fun handlePlaceholder(entity: Entity, args: String): String {
        if (entity !is LivingEntity || !entity.isAlive) return "Error entity: ${entity.name}"

        val splits = if (args.contains(":")) args.split(":") else args.split("_")
        val attrName = splits.getOrNull(0) ?: return "Error attribute: $args"

        val valueType = AttributeValueType.of(splits.getOrNull(1) ?: "RANDOM") ?: return "Error value type: ${splits[1]}"
        val digits = splits.getOrNull(2)?.toInt() ?: 2

        return AttributeBridge.getAttrValue(entity, attrName, valueType).format(digits).toString()
    }

    abstract fun registerSkill(e: E)

    abstract fun registerPlaceholder()

    companion object {

        val ignoreSkillArgs by lazy { hashSetOf("type", "source", "timeout") }
        val ignoreScriptArgs by lazy { hashSetOf("script", "s") }

        lateinit var hooker: AbstractMythicMobsHooker<*, *>

        @Awake(LifeCycle.ACTIVE)
        fun init() {
            hooker = when (MythicMobVersion.version) {
                MythicMobVersion.IV -> MythicMobsIVHookerImpl(MythicMobs.inst())
                MythicMobVersion.V -> MythicMobsVHookerImpl(MythicBukkit.inst())
                else -> return
            }

            hooker.registerPlaceholder()
        }

        fun parseArgsMap(entries: Set<Map.Entry<String, String>>): HashMap<String, Any> {
            val map = hashMapOf<String, Any>()
            entries.forEach { entry ->
                if (entry.key in ignoreScriptArgs) return@forEach
                map[entry.key] = entry.value
            }
            return map
        }

    }

}
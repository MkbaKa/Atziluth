package me.mkbaka.atziluth.internal.module.hook.mythicmobs

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyEntityTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptCondition
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import taboolib.common.io.runningClasses
import taboolib.common.platform.function.console
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common5.format
import taboolib.module.lang.sendLang
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

abstract class AbstractMythicMobsHooker {

    init {
        val packageName = this::class.java.`package`.name
        runningClasses.forEach { clazz ->
            if (!clazz.`package`.name.startsWith(packageName)) return@forEach

            registerClass(clazz)
        }

        registerMechanicListener()
        registerConditionListener()
        registerTargetListener()
        registerPlaceholder()

        registerBukkitListener(reloadEvent) { event ->
            onReload()
        }

    }

    abstract val reloadEvent: Class<out Event>

    abstract fun onReload()

    abstract fun registerMechanicListener()

    abstract fun registerConditionListener()

    abstract fun registerTargetListener()

    abstract fun registerPlaceholder()

    fun handlePlaceholder(entity: Entity, args: String): String {
        if (entity !is LivingEntity || !entity.isAlive) return "Error entity: ${entity.name}"

        val splits = if (args.contains(":")) args.split(":") else args.split("_")
        val attrName = splits.getOrNull(0) ?: return "Error attribute: $args"

        val valueType = AttributeValueType.of(splits.getOrNull(1) ?: "RANDOM")
            ?: return "Error value type: ${splits[1]}"
        val digits = splits.getOrNull(2)?.toInt() ?: 2

        return entity.getAttrValue(attrName, valueType).format(digits).toString()
    }

    fun registerClass(clazz: Class<*>) {
        when {
            clazz.isAnnotationPresent(MythicAnnotations.SkillMechanic::class.java) -> {
                val mechanic = clazz.getAnnotation(MythicAnnotations.SkillMechanic::class.java)
                mechanic.names.forEach { name -> skillClasses.computeIfAbsent(name) { clazz } }
                console().sendLang("register-mechanic", prefix, mechanic.names[0])
            }

            clazz.isAnnotationPresent(MythicAnnotations.SkillCondition::class.java) -> {
                val condition = clazz.getAnnotation(MythicAnnotations.SkillCondition::class.java)
                condition.names.forEach { name -> conditionClasses.computeIfAbsent(name) { clazz } }
                console().sendLang("register-condition", prefix, condition.names[0])
            }

            clazz.isAnnotationPresent(MythicAnnotations.SkillTarget::class.java) -> {
                val target = clazz.getAnnotation(MythicAnnotations.SkillTarget::class.java)
                target.names.forEach { name -> targetClasses.computeIfAbsent(name) { clazz } }
                console().sendLang("register-targeters", prefix, target.names[0])
            }
        }
    }

    companion object {

        val number_pattern by lazy { Pattern.compile("(-*\\d+)((-)(-*\\d+))*") }

        val scriptMechanics = ConcurrentHashMap<String, ProxyScriptMechanic>()

        val scriptConditions = ConcurrentHashMap<String, ProxyScriptCondition>()

        val scriptLocationTargeters = ConcurrentHashMap<String, ProxyLocationTargeter>()

        val scriptEntityTargeters = ConcurrentHashMap<String, ProxyEntityTargeter>()

        val skillClasses = ConcurrentHashMap<String, Class<*>>()

        val conditionClasses = ConcurrentHashMap<String, Class<*>>()

        val targetClasses = ConcurrentHashMap<String, Class<*>>()

    }

}
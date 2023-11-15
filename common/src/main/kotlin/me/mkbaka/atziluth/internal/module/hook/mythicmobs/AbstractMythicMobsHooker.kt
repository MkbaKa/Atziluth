package me.mkbaka.atziluth.internal.module.hook.mythicmobs

import me.mkbaka.atziluth.Atziluth.prefix
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyEntityTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptCondition
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import me.mkbaka.atziluth.utils.enumOf
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import taboolib.common.io.runningClasses
import taboolib.common.platform.function.console
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common5.format
import taboolib.module.lang.sendLang
import java.util.concurrent.ConcurrentHashMap

/**
 * MythicMobs 兼容
 */
abstract class AbstractMythicMobsHooker {

    init {
        // 注册 Atziluth 本体提供的技能，条件，选择器
        val packageName = this::class.java.`package`.name
        runningClasses.forEach { clazz ->
            if (!clazz.`package`.name.startsWith(packageName)) return@forEach

            registerClass(clazz)
        }

        registerMechanicListener()
        registerConditionListener()
        registerTargetListener()
        registerPlaceholder()

        // MythicMobs 插件重载时触发回调
        registerBukkitListener(reloadEvent) { event ->
            onReload()
        }

    }

    /**
     * MythicMobs 插件实例
     * 一般用于js 所以不用考虑类型
     */
    abstract val instance: Any

    /**
     * 重载事件
     */
    abstract val reloadEvent: Class<out Event>

    /**
     * 重载回调
     */
    abstract fun onReload()

    /**
     * 注册技能加载监听器
     */
    abstract fun registerMechanicListener()

    /**
     * 注册条件加载监听器
     */
    abstract fun registerConditionListener()

    /**
     * 注册目标选择器加载监听器
     */
    abstract fun registerTargetListener()

    /**
     * 注册占位符
     */
    abstract fun registerPlaceholder()


    /**
     * 处理占位符
     * @param [entity] 实体
     * @param [args] 参数
     * @return [String]
     */
    fun handlePlaceholder(entity: Entity, args: String): String {
        if (entity !is LivingEntity || !entity.isAlive) return "Error entity: ${entity.name}"

        val splits = if (args.contains(":")) args.split(":") else args.split("_")
        val attrName = splits.getOrNull(0) ?: return "Error attribute: $args"

        val valueType = enumOf<AttributeValueType>(splits.getOrNull(1) ?: "RANDOM")
            ?: return "Error value type: ${splits[1]}"
        val digits = splits.getOrNull(2)?.toInt() ?: 2

        return entity.getAttrValue(attrName, valueType).format(digits).toString()
    }

    /**
     * 通过class来注册技能，条件，目标选择器
     * @param [clazz]
     */
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

        /**
         * 脚本注册的技能
         */
        val scriptMechanics = ConcurrentHashMap<String, ProxyScriptMechanic>()

        /**
         * 脚本注册的条件
         */
        val scriptConditions = ConcurrentHashMap<String, ProxyScriptCondition>()

        /**
         * 脚本注册的坐标类选择器
         */
        val scriptLocationTargeters = ConcurrentHashMap<String, ProxyLocationTargeter>()

        /**
         * 脚本注册的实体类选择器
         */
        val scriptEntityTargeters = ConcurrentHashMap<String, ProxyEntityTargeter>()

        /**
         * 插件注册的技能
         */
        val skillClasses = ConcurrentHashMap<String, Class<*>>()

        /**
         * 插件注册的条件
         */
        val conditionClasses = ConcurrentHashMap<String, Class<*>>()

        /**
         * 插件注册的选择器
         */
        val targetClasses = ConcurrentHashMap<String, Class<*>>()

    }

}
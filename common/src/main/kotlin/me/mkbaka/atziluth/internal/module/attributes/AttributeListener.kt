package me.mkbaka.atziluth.internal.module.attributes

import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import me.mkbaka.atziluth.utils.EventUtil.isProjectileDamage
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.service.PlatformExecutor
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 属性监听器
 */
abstract class AttributeListener<BeforeUpdateEvent : Event, AfterUpdateEvent : Event> {

    init {
        // 缓存抛射物的射击者
        registerBukkitListener(ProjectileLaunchEvent::class.java, EventPriority.MONITOR) { event ->
            event.entity.shooter?.let { if (it is LivingEntity) shooters[event.entity.uniqueId] = it.uniqueId }
        }

        // 缓存箭矢的射击者
        registerBukkitListener(EntityShootBowEvent::class.java, EventPriority.MONITOR) { event ->
            // ProjectileLaunchEvent 会先于 EntityShootBowEvent 触发 若已缓存过射击者则不再缓存
            if (!shooters.containsKey(event.projectile.uniqueId)) shooters[event.projectile.uniqueId] = event.entity.uniqueId
            // 缓存蓄力值
            arrowForce[event.projectile.uniqueId] = event.force
        }

        // 抛射物命中时清除缓存
        registerBukkitListener(ProjectileHitEvent::class.java) { event ->
            // 在 未命中实体 且 命中方块 的情况下清除缓存
            if (shooters.containsKey(event.entity.uniqueId) && event.hitEntity == null && event.hitBlock != null) {
                shooters.remove(event.entity.uniqueId)
                arrowForce.remove(event.entity.uniqueId)
            }
        }

        // 伤害处理
        registerBukkitListener(EntityDamageByEntityEvent::class.java, EventPriority.HIGH) { event ->
            // 如果正在使用DamageMeta 则不触发属性 防止递归
            if (event.damager.hasMetadata("Atziluth:Vanilla_Damaging")) return@registerBukkitListener
            // 防止攻击展示框之类奇奇怪怪的玩意时触发属性
            if (event.entity !is LivingEntity) return@registerBukkitListener
            // 防止喷溅药水之类的玩意触发伤害导致拿不到射击者报错
            if (!event.isProjectileDamage() && event.damager !is LivingEntity) return@registerBukkitListener
            // 处理所有 isBefore 的 attack 与 defense 属性
            handlePre(event, AttributeManagerComponent.priorityMap.values.filter { attr -> attr.isBefore && filter(attr) {
                 this.attributeType == CustomAttributeType.ATTACK || this.attributeType == CustomAttributeType.DEFENSE
            } })
        }

        // 伤害处理
        registerBukkitListener(EntityDamageByEntityEvent::class.java, EventPriority.MONITOR) { event ->
            if (event.damager.hasMetadata("Atziluth:Vanilla_Damaging")) return@registerBukkitListener
            if (event.entity !is LivingEntity) return@registerBukkitListener
            if (!event.isProjectileDamage() && event.damager !is LivingEntity) return@registerBukkitListener
            // 处理所有非 isBefore 的 attack 与 defense 属性
            handlePost(event, AttributeManagerComponent.priorityMap.values.filter { attr -> !attr.isBefore && filter(attr) {
                this.attributeType == CustomAttributeType.ATTACK || this.attributeType == CustomAttributeType.DEFENSE
            } })
            // 在处理后清除抛射物的缓存
            if (event.isProjectileDamage()) {
                shooters.remove(event.damager.uniqueId)
                arrowForce.remove(event.damager.uniqueId)
            }
        }

        // 处理 isBefore 的 update 类属性
        registerBukkitListener(beforeUpdateEvent, EventPriority.MONITOR) { event ->
            handleUpdateBefore(event, AttributeManagerComponent.priorityMap.values.filter { attr -> attr.isBefore && filter(attr) {
                this.attributeType == CustomAttributeType.UPDATE
            } })
        }

        // 处理非 isBefore 的 update 类属性
        registerBukkitListener(afterUpdateEvent, EventPriority.MONITOR) { event ->
            handleUpdateAfter(event, AttributeManagerComponent.priorityMap.values.filter { attr -> !attr.isBefore && filter(attr) {
                this.attributeType == CustomAttributeType.UPDATE
            } })
        }

    }

    /**
     * runtime属性类型调度器
     */
    lateinit var runtimeTask: PlatformExecutor.PlatformTask

    /**
     * 调度执行时间
     */
    var time = 0

    /**
     * 属性更新事件前
     * 用于触发 Update 类型
     */
    abstract val beforeUpdateEvent: Class<BeforeUpdateEvent>

    /**
     * 属性更新事件后
     * 用于触发 Update 类型
     */
    abstract val afterUpdateEvent: Class<AfterUpdateEvent>

    /**
     * 处理 isBefore 的 Attack 及 Defense 属性
     * @param [event] 实体
     * @param [attrs] 属性列表
     */
    abstract fun handlePre(event: EntityDamageByEntityEvent, attrs: Collection<CustomAttribute>)

    /**
     * 处理非 isBefore 的 Attack 及 Defense 属性
     * @param [event] 实体
     * @param [attrs] 属性列表
     */
    abstract fun handlePost(event: EntityDamageByEntityEvent, attrs: Collection<CustomAttribute>)

    /**
     * 处理 isBefore 的 Update 类 属性
     * @param [event] 实体
     * @param [attrs] 属性列表
     */
    abstract fun handleUpdateBefore(event: BeforeUpdateEvent, attrs: Collection<CustomAttribute>)

    /**
     * 处理非 isBefore 的 Update 类 属性
     * @param [event] 实体
     * @param [attrs] 属性列表
     */
    abstract fun handleUpdateAfter(event: AfterUpdateEvent, attrs: Collection<CustomAttribute>)

    /**
     * 过滤属性
     */
    private fun filter(attr: CustomAttribute, callback: CustomAttribute.() -> Boolean): Boolean {
        return attr.priority > 0 && callback(attr)
    }

    companion object {

        /**
         * 抛射物射击者
         */
        val shooters = ConcurrentHashMap<UUID, UUID>()

        /**
         * 箭矢力度
         */
        val arrowForce = ConcurrentHashMap<UUID, Float>()

    }

}
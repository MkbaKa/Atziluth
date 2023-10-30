package me.mkbaka.atziluth.internal.module.attributes

import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import org.bukkit.event.Event
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.service.PlatformExecutor

abstract class AttributeListener<DamageEvent : Event, BeforeUpdateEvent : Event, AfterUpdateEvent : Event> {

    init {

        registerBukkitListener(damageEvent, EventPriority.HIGH) { event ->
            handlePre(event, AttributeManagerComponent.priorityMap.values.filter { attr -> attr.isBefore && filter(attr) {
                 this.attributeType == CustomAttributeType.ATTACK || this.attributeType == CustomAttributeType.DEFENSE
            } })
        }

        registerBukkitListener(damageEvent, EventPriority.MONITOR) { event ->
            handlePost(event, AttributeManagerComponent.priorityMap.values.filter { attr -> !attr.isBefore && filter(attr) {
                this.attributeType == CustomAttributeType.ATTACK || this.attributeType == CustomAttributeType.DEFENSE
            } })
        }

        registerBukkitListener(beforeUpdateEvent, EventPriority.MONITOR) { event ->
            handleUpdateBefore(event, AttributeManagerComponent.priorityMap.values.filter { attr -> attr.isBefore && filter(attr) {
                this.attributeType == CustomAttributeType.UPDATE
            } })
        }

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
     * 伤害事件
     * 用于触发 Attack 及 Defense 类型
     */
    abstract val damageEvent: Class<DamageEvent>

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
    abstract fun handlePre(event: DamageEvent, attrs: Collection<CustomAttribute>)

    /**
     * 处理非 isBefore 的 Attack 及 Defense 属性
     * @param [event] 实体
     * @param [attrs] 属性列表
     */
    abstract fun handlePost(event: DamageEvent, attrs: Collection<CustomAttribute>)

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

}
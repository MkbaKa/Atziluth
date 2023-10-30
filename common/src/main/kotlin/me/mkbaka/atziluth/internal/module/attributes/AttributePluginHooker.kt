package me.mkbaka.atziluth.internal.module.attributes

import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.configuration.ConfigurationManager
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeDataManager
import me.mkbaka.atziluth.utils.EventUtil.getAttackCooldown
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.function.submitAsync

abstract class AttributePluginHooker<BeforeUpdateEvent : Event, AfterUpdateEvent: Event> : AttributeListener<EntityDamageByEntityEvent, BeforeUpdateEvent, AfterUpdateEvent>() {

    init {
        this.runtimeTask = submitAsync(period = 20) {

            val attributes = AttributeManagerComponent.priorityMap.values.filter {
                attr -> attr.priority > 0 && attr.attributeType == CustomAttributeType.RUNTIME && ++super.time % attr.period == 0
            }

            Bukkit.getOnlinePlayers().forEach { player ->
                attributes.forEach { attr ->
                    if (attr.skipFilter || player.getAttrValue(attr.attributeName) > 0.0) {
                        attr.run(player, attr)
                    }
                }
            }

            if (super.time >= ConfigurationManager.runtimeResetPeriod) super.time = 0

        }
    }

    /**
     * 属性数据操作
     */
    abstract val attributeDataManager: AttributeDataManager<*>

    /**
     * 注册属性
     */
    abstract fun registerOtherAttribute(name: String, combatPower: Double, placeholder: String)

    /**
     * 重载
     */
    abstract fun reload()

    override val damageEvent: Class<EntityDamageByEntityEvent>
        get() = EntityDamageByEntityEvent::class.java

    override fun handlePre(event: EntityDamageByEntityEvent, attrs: Collection<CustomAttribute>) {
        val damager = event.damager as? LivingEntity ?: return
        val entity = event.entity as? LivingEntity ?: return
        val args = hashMapOf("event" to event, "force" to event.getAttackCooldown())
        attrs.forEach { attr ->
            handleDamageAttribute(damager, entity, attr, args)
        }
    }

    override fun handlePost(event: EntityDamageByEntityEvent, attrs: Collection<CustomAttribute>) {
        handlePre(event, attrs)
    }

    open fun handleDamageAttribute(damager: LivingEntity, entity: LivingEntity, attr: CustomAttribute, args: Map<String, Any>) {
        if (attr.skipFilter) return attr.callback(damager, entity, attr, args)

        val attributeValue = when (attr.attributeType) {
            CustomAttributeType.DEFENSE -> entity.getAttrValue(attr.attributeName)
            else -> damager.getAttrValue(attr.attributeName)
        }

        if (attributeValue > 0.0) attr.callback(damager, entity, attr, args)
    }

}
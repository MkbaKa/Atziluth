package me.mkbaka.atziluth.internal.module.attributes

import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.internal.configuration.ConfigurationManager
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeDataManager
import me.mkbaka.atziluth.internal.module.fightdata.impl.FightDataImpl
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.function.submitAsync

abstract class AttributePluginHooker<BeforeUpdateEvent : Event, AfterUpdateEvent : Event> : AttributeListener<BeforeUpdateEvent, AfterUpdateEvent>() {

    init {
        this.runtimeTask = submitAsync(period = 20) {

            val attributes = AttributeManagerComponent.priorityMap.values.filter {
                attr -> attr.priority > 0 && attr.attributeType == CustomAttributeType.RUNTIME && ++super.time % attr.period == 0
            }

            Bukkit.getOnlinePlayers().forEach { player ->
                attributes.forEach { attr ->
                    if (attr.skipFilter || player.getAttrValue(attr.attributeName) > 0.0) {
                        attr.run(player)
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
     * 获取所有属性名
     */
    abstract fun getAllAttributes(whiteListAttribute: List<String> = emptyList()): Collection<String>

    /**
     * 重载
     */
    abstract fun reload()

    override fun handlePre(event: EntityDamageByEntityEvent, attrs: Collection<CustomAttribute>) {
        event.damage = FightDataImpl(event).also {
            it.attributes.addAll(attrs)
            it.handle()
        }.damageValue
    }

    override fun handlePost(event: EntityDamageByEntityEvent, attrs: Collection<CustomAttribute>) {
        handlePre(event, attrs)
    }

}
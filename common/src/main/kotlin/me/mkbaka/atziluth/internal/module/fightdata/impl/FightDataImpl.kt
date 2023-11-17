package me.mkbaka.atziluth.internal.module.fightdata.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.api.AttributeAPI.getMaxValue
import me.mkbaka.atziluth.api.AttributeAPI.getMinValue
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
import me.mkbaka.atziluth.internal.module.fightdata.FightData
import me.mkbaka.atziluth.internal.module.tempdatamanager.data.EntityData
import me.mkbaka.atziluth.utils.AttributeUtil.append
import me.mkbaka.atziluth.utils.AttributeUtil.getOrDef
import me.mkbaka.atziluth.utils.AttributeUtil.mapBy
import me.mkbaka.atziluth.utils.EventUtil.getAttackCooldown
import me.mkbaka.atziluth.utils.EventUtil.getAttacker
import me.mkbaka.atziluth.utils.EventUtil.isProjectileDamage
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.util.random
import java.util.*

class FightDataImpl : FightData {

    constructor(event: EntityDamageByEntityEvent) {
        this.event = event
        this.attacker = event.getAttacker()!!
        this.entity = event.entity as LivingEntity
        this.projectile = if (event.isProjectileDamage()) event.damager as Projectile else null
        this.attributes = mutableListOf()
        this.attributeData = hashMapOf<UUID, MutableMap<String, DoubleArray>>().also {
            it[attacker.uniqueId] = hashMapOf()
            it[entity.uniqueId] = hashMapOf()
        }
        this.tempData = hashMapOf()
        this.damageValue = event.finalDamage
        this.force = event.getAttackCooldown()
        this.isCancelled = false
        initAttributes()
    }

    constructor(attacker: LivingEntity, entity: LivingEntity) {
        this.event = EntityDamageByEntityEvent(attacker, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0.0)
        this.attacker = attacker
        this.entity = entity
        this.projectile = if (attacker is Projectile) attacker else null
        this.attributes = mutableListOf()
        this.attributeData = hashMapOf<UUID, MutableMap<String, DoubleArray>>().also {
            it[attacker.uniqueId] = hashMapOf()
            it[entity.uniqueId] = hashMapOf()
        }
        this.tempData = hashMapOf()
        this.damageValue = 1.0
        this.force = 1.0
        this.isCancelled = false
        initAttributes()
    }

    override val event: EntityDamageByEntityEvent

    override val attacker: LivingEntity

    override val entity: LivingEntity

    override var projectile: Projectile?

    override val attributes: MutableList<CustomAttribute>

    override val attributeData: MutableMap<UUID, MutableMap<String, DoubleArray>>

    override val tempData: MutableMap<UUID, EntityData>

    override var damageValue: Double

    override val force: Double

    override var isCancelled: Boolean

    /**
     * 此次事件是否由远程伤害触发
     * @return [Boolean]
     */
    fun isProjectileDamage(): Boolean {
        return event.isProjectileDamage()
    }

    /**
     * 存储数据
     * 仅此次处理中可用
     * @param [uuid] 数据归属者的uuid
     * @param [key] key
     * @param [value] value
     */
    fun saveData(uuid: UUID, key: String, value: Any) {
        this.tempData.getOrPut(uuid) { EntityData(uuid) }.saveData(key, value)
    }

    /**
     * 获取数据
     * @param [uuid] 数据归属者的uuid
     * @param [key] key
     * @return [Any?]
     */
    fun getData(uuid: UUID, key: String): Any? {
        return this.tempData[uuid]?.getData(key)
    }

    /**
     * 获取此次攻击的蓄力进度
     * 0 ~ 1
     * 仅对玩家生效
     * @return [Double]
     */
    fun getFore(): Double {
        return this.force
    }

    /**
     * 增加此次伤害
     * @param [value] 伤害值
     */
    fun addDamage(value: Double) {
        this.damageValue += value
    }

    /**
     * 减少此次伤害
     * @param [value] 伤害值
     */
    fun takeDamage(value: Double) {
        setDamage(this.damageValue - value)
    }

    /**
     * 设置此次伤害
     * @param [value] 伤害值
     */
    fun setDamage(value: Double) {
        this.damageValue = value.coerceAtLeast(0.0)
    }

    /**
     * 获取此次伤害
     * @return [Double] 伤害值
     */
    fun getDamage(): Double {
        return this.damageValue
    }

    /**
     * 操作当前事件触发时的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [value] 属性值
     */
    fun addAttribute(entity: LivingEntity, name: String, value: Double) {
        attributeData[entity.uniqueId]?.compute(name) { _, oldValue ->
            (oldValue ?: doubleArrayOf(0.0, 0.0)).mapBy { it + value }
        }
    }

    /**
     * 操作当前事件触发时的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [value] 属性值
     */
    fun addAttribute(entity: LivingEntity, name: String, values: DoubleArray) {
        attributeData[entity.uniqueId]?.compute(name) { _, oldValue ->
            (oldValue ?: doubleArrayOf(0.0, 0.0)).append(values)
        }
    }

    /**
     * 操作当前事件触发时的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [value] 属性值
     */
    fun setAttribute(entity: LivingEntity, name: String, value: Double) {
        attributeData[entity.uniqueId]?.compute(name) { _, oldValue ->
            (oldValue ?: doubleArrayOf(0.0, 0.0)).mapBy { value }
        }
    }

    /**
     * 操作当前事件触发时的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [value] 属性值
     */
    fun setAttribute(entity: LivingEntity, name: String, values: DoubleArray) {
        attributeData[entity.uniqueId]?.set(name, values)
    }

    /**
     * 操作当前事件触发时的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [value] 属性值
     */
    fun takeAttribute(entity: LivingEntity, name: String, value: Double) {
        attributeData[entity.uniqueId]?.compute(name) { _, oldValue ->
            (oldValue ?: doubleArrayOf(0.0, 0.0)).mapBy { it - value }
        }
    }

    /**
     * 操作当前事件触发时的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [value] 属性值
     */
    fun takeAttribute(entity: LivingEntity, name: String, values: DoubleArray) {
        if (attributeData.containsKey(entity.uniqueId) && attributeData[entity.uniqueId]!!.containsKey(name)) {
            attributeData[entity.uniqueId]!!.compute(name) { _, oldValue ->
                oldValue!!.apply { forEachIndexed { index, _ -> oldValue[index] -= values.getOrDef(index, 0.0) } }
            }
        }
    }

    /**
     * 获取战斗数据中的属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @param [valueType] 属性值类型
     * @return [Double]
     */
    fun getAttribute(entity: LivingEntity, name: String, valueType: AttributeValueType = AttributeValueType.RANDOM): Double {
        val values = attributeData[entity.uniqueId]?.get(name) ?: return 0.0
        return when (valueType) {
            AttributeValueType.MIN -> values.getOrDef(0, 0.0)
            AttributeValueType.MAX -> values.getOrDef(1, 0.0)
            AttributeValueType.RANDOM -> random(values.getOrDef(0, 0.0), values.getOrDef(1, 0.0))
        }
    }

    /**
     * 处理攻击和防御类属性
     */
    fun handle() {
        attributes.forEach { attr ->
            val values = when (attr.attributeType) {
                CustomAttributeType.DEFENSE -> getAttributeValues(entity, attr.attributeName)
                CustomAttributeType.ATTACK -> getAttributeValues(attacker, attr.attributeName)
                else -> null
            }

            if (values == null && !attr.skipFilter) return@forEach

            callAttribute(attr, values ?: doubleArrayOf(0.0, 0.0))

            if (isCancelled) return
        }
    }

    /**
     * 尝试运行指定属性
     * 仅对 Atziluth 属性生效
     * * values内的值 会 附加给该属性
     * * 触发后删除
     * @param [attrName] 属性名
     * @param [values] 数值
     */
    fun callAttribute(attrName: String, values: DoubleArray) {
        AttributeManagerComponent.attributes[attrName]?.let { attr ->
            addAttribute(entity, attr.attributeName, values)
            callAttribute(attr, values)
            takeAttribute(entity, attr.attributeName, values)
        }
    }

    /**
     * 尝试运行指定属性
     * 仅对 Atziluth 属性生效
     * * values内的值 不会 附加给该属性
     * @param [attr] 属性实例
     * @param [values] 数值
     */
    fun callAttribute(attr: CustomAttribute, values: DoubleArray) {
        if (attr.skipFilter || values.getOrDef(1, values.getOrDef(0, 0.0)) > 0.0) {
            attr.callback(this, hashMapOf(
                "event" to event,
                "force" to event.getAttackCooldown())
            )
        }
    }

    /**
     * 获取属性值
     * @param [entity] 实体
     * @param [name] 属性名
     * @return [DoubleArray?]
     */
    fun getAttributeValues(entity: LivingEntity, name: String): DoubleArray? {
        return attributeData[entity.uniqueId]?.get(name)
    }

    /**
     * 获取随机值 (minValue ~ maxValue)
     * @param [entity] 实体
     * @param [name] 属性名
     * @return [Double]
     */
    fun getRandomValue(entity: LivingEntity, name: String): Double {
        val values = attributeData[entity.uniqueId]?.get(name) ?: return 0.0
        val min = values.getOrDef(0, 0.0)
        return random(min, values.getOrDef(1, min))
    }

    /**
     * 初始化攻击和受击实体的属性值
     */
    private fun initAttributes() {
        Atziluth.attributeHooker.getAllAttributes().forEach { name ->
            if (attacker.getMaxValue(name) > 0.0) {
                attributeData[attacker.uniqueId]!![name] =
                    doubleArrayOf(attacker.getMinValue(name), attacker.getMaxValue(name))
            }
            if (entity.getMaxValue(name) > 0.0) {
                attributeData[entity.uniqueId]!![name] =
                    doubleArrayOf(entity.getMinValue(name), entity.getMaxValue(name))
            }
        }
    }

}
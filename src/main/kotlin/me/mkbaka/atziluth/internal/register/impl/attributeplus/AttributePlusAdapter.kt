package me.mkbaka.atziluth.internal.register.impl.attributeplus

import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeType
import me.mkbaka.atziluth.internal.register.BaseAttribute
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.api.component.SubAttribute
import org.serverct.ersha.attribute.AttributeHandle
import taboolib.common5.cdouble

class AttributePlusAdapter(
    override val attrPriority: Int,
    override val name: String,
    override val placeholder: String,
    override val combatPower: Double,
    override val type: AttributeType
) : AbstractCustomAttribute<SubAttribute>() {

    override val inst: SubAttribute by lazy {
        object : SubAttribute(
            attrPriority, combatPower, name, getType(), placeholder
        ), BaseAttribute<AttributeHandle> {

            override fun onLoad(): SubAttribute {
                onLoad?.let { it(this@AttributePlusAdapter) }
                skipFilter.setSkipFilter()
                return this
            }

            override fun runAttack(attacker: LivingEntity, entity: LivingEntity, handle: AttributeHandle): Boolean {
                return callback?.let { it(this@AttributePlusAdapter, attacker, entity, hashMapOf("handle" to handle)) } ?: false
            }

            override fun runDefense(entity: LivingEntity, killer: LivingEntity, handle: AttributeHandle): Boolean {
                return callback?.let { it(this@AttributePlusAdapter, entity, killer, hashMapOf("handle" to handle)) } ?: false
            }

            // 用一种比较弱智的方法修复了这几个sb东西
            // 我真的想不通 为什么不传递这个handle一直报错未初始化
            override fun getFinalDamage(attacker: LivingEntity, handle: AttributeHandle): Double {
                return handle.getDamage(attacker).cdouble
            }

            override fun addFinalDamage(attacker: LivingEntity, value: Double, handle: AttributeHandle) {
                val damage = getFinalDamage(attacker, handle)
                handle.setDamage(attacker, damage + value)
            }

            override fun takeFinalDamage(attacker: LivingEntity, value: Double, handle: AttributeHandle) {
                val damage = getFinalDamage(attacker, handle)
                handle.setDamage(attacker, damage - value)
            }

            override fun setFinalDamage(attacker: LivingEntity, value: Double, handle: AttributeHandle) {
                handle.setDamage(attacker, value)
            }

            override val isProjectile: Boolean
                get() = isProjectileDamage()
        }
    }

    override fun register() {
        AttributePlusImpl.registerAttribute(this)
    }

    private fun getType(): org.serverct.ersha.attribute.enums.AttributeType {
        return when (type) {
            AttributeType.ATTACK -> org.serverct.ersha.attribute.enums.AttributeType.ATTACK
            AttributeType.DEFENSE -> org.serverct.ersha.attribute.enums.AttributeType.DEFENSE
            AttributeType.RUNTIME, AttributeType.OTHER -> org.serverct.ersha.attribute.enums.AttributeType.OTHER
        }
    }
}
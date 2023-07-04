package me.mkbaka.atziluth.internal.register.impl.attributeplus

import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeType
import me.mkbaka.atziluth.internal.register.BaseAttribute
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.api.component.SubAttribute
import taboolib.common5.cdouble

class AttributePlusAdapter(
    override val attrPriority: Int,
    override val name: String,
    override val placeholder: String,
    override val combatPower: Double,
    override val type: AttributeType
) : AbstractCustomAttribute<SubAttribute>() {

    override val inst: SubAttribute

    init {
        inst = object : SubAttribute(
            attrPriority, combatPower, name, getType(), placeholder
        ), BaseAttribute {

            override fun onLoad(): SubAttribute {
                onLoad?.let { it(this@AttributePlusAdapter) }
                skipFilter.setSkipFilter()
                return this
            }

            override fun runAttack(attacker: LivingEntity, entity: LivingEntity): Boolean {
                return callback?.let { it(this@AttributePlusAdapter, attacker, entity) } ?: false
            }

            override fun runDefense(entity: LivingEntity, killer: LivingEntity): Boolean {
                return callback?.let { it(this@AttributePlusAdapter, entity, killer) } ?: false
            }

            override fun getFinalDamage(attacker: LivingEntity): Double {
                return attacker.getDamage().cdouble
            }

            override fun addFinalDamage(attacker: LivingEntity, value: Double) {
                attacker.addDamage(value)
            }

            override fun takeFinalDamage(attacker: LivingEntity, value: Double) {
                attacker.takeDamage(value)
            }

            override fun setFinalDamage(attacker: LivingEntity, value: Double) {
                attacker.setDamage(value)
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
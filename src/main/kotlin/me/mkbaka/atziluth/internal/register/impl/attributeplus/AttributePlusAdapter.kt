package me.mkbaka.atziluth.internal.register.impl.attributeplus

import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeType
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.api.component.SubAttribute

class AttributePlusAdapter(
    override val attrPriority: Int,
    override val name: String,
    override val placeholder: String,
    override val combatPower: Double,
    override val type: AttributeType
) : AbstractCustomAttribute<SubAttribute, org.serverct.ersha.attribute.enums.AttributeType>() {

    override fun build(): SubAttribute {
        return object : SubAttribute(
            attrPriority, combatPower, name, getType(), placeholder
        ) {
            override fun onLoad(): SubAttribute {
                onLoad?.let { it(this) }
                return this
            }

            override fun runAttack(attacker: LivingEntity, entity: LivingEntity): Boolean {
                return callback?.let { it(this, attacker, entity) } ?: false
            }

            override fun runDefense(entity: LivingEntity, killer: LivingEntity): Boolean {
                return callback?.let { it(this, entity, killer) } ?: false
            }
        }
    }

    override fun register() {
        AttributePlusImpl.registerAttribute(this)
    }

    override fun getType(): org.serverct.ersha.attribute.enums.AttributeType {
        return when (type) {
            AttributeType.ATTACK -> org.serverct.ersha.attribute.enums.AttributeType.ATTACK
            AttributeType.DEFENSE -> org.serverct.ersha.attribute.enums.AttributeType.DEFENSE
            AttributeType.RUNTIME, AttributeType.OTHER -> org.serverct.ersha.attribute.enums.AttributeType.OTHER
        }
    }
}
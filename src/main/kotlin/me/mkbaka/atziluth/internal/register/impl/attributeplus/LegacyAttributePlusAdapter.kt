package me.mkbaka.atziluth.internal.register.impl.attributeplus

import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.serverct.ersha.jd.api.BaseAttribute

class LegacyAttributePlusAdapter(
    override val name: String,
    override val placeholder: String,
    override val type: AttributeType
) : AbstractCustomAttribute<BaseAttribute, org.serverct.ersha.jd.api.AttributeType>() {

    override fun build(): BaseAttribute {
        return object : BaseAttribute(
            getType(), name, placeholder
        ), me.mkbaka.atziluth.internal.register.BaseAttribute {

            override fun run(attacker: Entity, entity: Entity, attributeValue: Double) {
                if (attacker !is LivingEntity || entity !is LivingEntity) return
                callback?.let { it(this, attacker, entity) }
            }

            override fun getFinalDamage(attacker: LivingEntity): Double {
                return this.damage
            }

            override fun addFinalDamage(attacker: LivingEntity, value: Double) {
                this.damage += value
            }

            override fun takeFinalDamage(attacker: LivingEntity, value: Double) {
                this.damage -= value
            }

            override fun setFinalDamage(attacker: LivingEntity, value: Double) {
                damage = value
            }

            override val isProjectile: Boolean
                get() = error("Unsupported method for AttributePlus v2.x")

        }
    }

    override fun register() {
        LegacyAttributePlusImpl.registerAttribute(this)
    }

    override fun getType(): org.serverct.ersha.jd.api.AttributeType {
        return when (type) {
            AttributeType.ATTACK -> org.serverct.ersha.jd.api.AttributeType.DAMAGE
            AttributeType.DEFENSE -> org.serverct.ersha.jd.api.AttributeType.INJURED
            AttributeType.RUNTIME, AttributeType.OTHER -> org.serverct.ersha.jd.api.AttributeType.NULL
        }
    }

}
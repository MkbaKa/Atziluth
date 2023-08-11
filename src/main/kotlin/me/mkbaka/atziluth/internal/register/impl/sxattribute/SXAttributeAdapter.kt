package me.mkbaka.atziluth.internal.register.impl.sxattribute

import github.saukiya.sxattribute.data.attribute.SubAttribute
import github.saukiya.sxattribute.data.eventdata.EventData
import github.saukiya.sxattribute.data.eventdata.sub.DamageData
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeType
import me.mkbaka.atziluth.internal.register.BaseAttribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class SXAttributeAdapter(
    override val attrPriority: Int,
    override val name: String,
    override val placeholder: String,
    override val type: AttributeType
) : AbstractCustomAttribute<SubAttribute>() {

    override val inst: SubAttribute by lazy {
        object : SubAttribute(name, Atziluth.plugin, 2, getType()), BaseAttribute<SubAttribute> {
            init {
                this.priority = getAttributes().size
                onLoad?.let { it(this@SXAttributeAdapter) }
            }

            private var damage = 0.0

            override fun eventMethod(values: DoubleArray, data: EventData) {
                if (data !is DamageData) return
                this.damage = data.damage

                when (type) {
                    AttributeType.ATTACK -> callback?.let { it(this@SXAttributeAdapter, data.attacker, data.defender, hashMapOf("handle" to this)) }
                    AttributeType.DEFENSE -> callback?.let { it(this@SXAttributeAdapter, data.defender, data.attacker, hashMapOf("handle" to this)) }
                    else -> error("Unsupported type $type")
                }
                data.damage = damage
            }

            override fun getPlaceholder(values: DoubleArray, player: Player, str: String): Any {
                return if (str.equals(placeholder, ignoreCase = true)) {
                    if (values[1] != 0.0) "${values[0]} - ${values[1]}" else values[0]
                } else "NaN"
            }

            override fun getPlaceholders(): MutableList<String> {
                return mutableListOf(name)
            }

            override fun loadAttribute(values: DoubleArray, lore: String) {
                if (lore.contains(name)) {
                    if (lore.contains("-")) {
                        val splits = lore.split("-")
                        splits.forEachIndexed { index, line ->
                            values[index] = getNumber(line)
                        }
                    } else {
                        values[0] = getNumber(lore)
                    }
                }
            }

            override fun getFinalDamage(attacker: LivingEntity, handle: SubAttribute): Double {
                return this.damage
            }

            override fun addFinalDamage(attacker: LivingEntity, value: Double, handle: SubAttribute) {
                this.damage += value
            }

            override fun takeFinalDamage(attacker: LivingEntity, value: Double, handle: SubAttribute) {
                this.damage -= value
            }

            override fun setFinalDamage(attacker: LivingEntity, value: Double, handle: SubAttribute) {
                this.damage = value
            }

            override val isProjectile: Boolean
                get() = error("Unsupported method for SX-Attribute v2.x")
        }
    }

    override fun register() {
        SXAttributeImpl.registerAttribute(this)
    }

    private fun getType(): github.saukiya.sxattribute.data.attribute.AttributeType {
        return when (type) {
            AttributeType.ATTACK -> github.saukiya.sxattribute.data.attribute.AttributeType.ATTACK
            AttributeType.DEFENSE -> github.saukiya.sxattribute.data.attribute.AttributeType.DEFENCE
            AttributeType.RUNTIME, AttributeType.OTHER -> github.saukiya.sxattribute.data.attribute.AttributeType.OTHER
        }
    }

}
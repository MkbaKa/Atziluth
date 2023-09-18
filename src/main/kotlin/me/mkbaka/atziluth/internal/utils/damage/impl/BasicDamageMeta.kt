package me.mkbaka.atziluth.internal.utils.damage.impl

import me.mkbaka.atziluth.internal.utils.damage.DamageMeta
import me.mkbaka.atziluth.internal.utils.damage.DamageOptions
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import java.util.*

open class BasicDamageMeta(
    override val damager: LivingEntity,
    override val defenders: Collection<LivingEntity>,
    override val damageOptions: DamageOptions
) : DamageMeta {

    override fun doDamage() {
        defenders.forEach { entity ->
            if (damageOptions.ignoreImmunity) entity.noDamageTicks = 0

            val action = {
                entity.damage(damageOptions.basicDamageValue, damager)
            }

            when {
                damageOptions.preventKnockback -> {
                    val modifier = AttributeModifier(
                        UUID.randomUUID(),
                        "Atziluth-preventKnockback",
                        1.0,
                        AttributeModifier.Operation.ADD_NUMBER
                    )
                    entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.addModifier(modifier)
                    action()
                    entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)!!.removeModifier(modifier)
                }

                else -> action()
            }

            entity.noDamageTicks = damageOptions.noDamageTicks
        }
    }

}
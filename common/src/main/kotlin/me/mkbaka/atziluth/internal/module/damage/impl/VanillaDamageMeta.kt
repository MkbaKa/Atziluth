package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.internal.module.damage.DamageMeta
import me.mkbaka.atziluth.internal.module.damage.DamageOptions
import me.mkbaka.atziluth.utils.SchedulerUtil.callSync
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import java.util.*

open class VanillaDamageMeta(
    override val damager: LivingEntity,
    override val entities: Collection<LivingEntity>,
    override val options: DamageOptions
) : DamageMeta {

    override fun doDamage() {
        this.entities.forEach { entity ->
            if (this.options.ignoreImmunity) entity.noDamageTicks = 0

            val damage = {
                callSync {
                    entity.damage(this.options.damageValue, this.damager)
                }
            }

            when {
                this.options.preventKnockback -> {
                    entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.let { instance ->
                        val modifier = AttributeModifier(UUID.randomUUID(), "Atziluth-doDamage-preventKnockback", 1.0, AttributeModifier.Operation.ADD_NUMBER)
                        instance.addModifier(modifier)
                        damage()
                        instance.removeModifier(modifier)
                    }
                }

                else -> damage()
            }

        }
    }

}
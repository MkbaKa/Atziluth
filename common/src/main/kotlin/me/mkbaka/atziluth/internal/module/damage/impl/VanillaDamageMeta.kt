package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.damage.DamageMeta
import me.mkbaka.atziluth.internal.module.damage.DamageOptions
import me.mkbaka.atziluth.utils.SchedulerUtil.callSync
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.FixedMetadataValue
import taboolib.platform.util.removeMeta
import java.util.*

open class VanillaDamageMeta(
    override val damager: LivingEntity,
    override val entities: Collection<LivingEntity>,
    override val options: DamageOptions
) : DamageMeta {

    override fun doDamage() {
        callSync {
            damager.setMetadata("Atziluth:Vanilla_Damaging", FixedMetadataValue(Atziluth.plugin, true))
            entities.forEach { entity ->
                if (options.ignoreImmunity) entity.noDamageTicks = 0

                val damage = {
                    entity.damage(options.damageValue, damager)
                }

                when {
                    options.preventKnockback -> {
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
            damager.removeMeta("Atziluth:Vanilla_Damaging")
        }

    }

}
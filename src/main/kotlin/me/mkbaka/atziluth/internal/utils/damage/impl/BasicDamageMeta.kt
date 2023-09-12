package me.mkbaka.atziluth.internal.utils.damage.impl

import me.mkbaka.atziluth.internal.utils.damage.DamageMeta
import me.mkbaka.atziluth.internal.utils.damage.DamageOptions
import org.bukkit.entity.LivingEntity

open class BasicDamageMeta(
    override val damager: LivingEntity,
    override val defenders: Collection<LivingEntity>,
    override val damageOptions: DamageOptions
) : DamageMeta {

    override fun doDamage() {
        defenders.forEach { entity ->
            entity.noDamageTicks = 0
            entity.damage(damageOptions.basicDamageValue, damager)
            entity.noDamageTicks = damageOptions.noDamageTicks
        }
    }

}
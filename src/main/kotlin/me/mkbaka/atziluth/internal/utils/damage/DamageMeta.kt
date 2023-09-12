package me.mkbaka.atziluth.internal.utils.damage

import me.mkbaka.atziluth.internal.utils.damage.impl.BasicDamageMeta
import org.bukkit.entity.LivingEntity

interface DamageMeta {

    val damager: LivingEntity

    val defenders: Collection<LivingEntity>

    val damageOptions: DamageOptions

    fun doDamage()

    companion object {

        fun createMeta(damager: LivingEntity, defender: LivingEntity, options: DamageOptions): DamageMeta {
            return BasicDamageMeta(damager, listOf(defender), options)
        }

        fun createMeta(damager: LivingEntity, defenders: Collection<LivingEntity>, options: DamageOptions): DamageMeta {
            return BasicDamageMeta(damager, defenders, options)
        }

    }

}
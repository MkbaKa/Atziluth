package me.mkbaka.atziluth.internal.module.damage

import org.bukkit.entity.LivingEntity

interface DamageMeta {

    val damager: LivingEntity

    val entities: Collection<LivingEntity>

    val options: DamageOptions

    fun doDamage()

}
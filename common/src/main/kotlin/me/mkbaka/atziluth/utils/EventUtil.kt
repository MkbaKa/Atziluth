package me.mkbaka.atziluth.utils

import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

object EventUtil {

    fun Event.call() {
        Bukkit.getPluginManager().callEvent(this)
    }

    fun EntityDamageByEntityEvent.getAttackCooldown(): Double {
        val originDamage = this.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE)
        if (this.damager is Projectile) return originDamage / 9

        val damager = this.damager as? LivingEntity ?: return 1.0
        return damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value?.let { value ->
            originDamage / value
        } ?: 1.0
    }

}
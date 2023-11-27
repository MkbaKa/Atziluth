package me.mkbaka.atziluth.utils

import me.mkbaka.atziluth.internal.module.attributes.AttributeListener
import me.mkbaka.atziluth.utils.EntityUtil.getLivingEntity
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.reflections.Reflections
import taboolib.common5.format

object EventUtil {

    val whiteListProjectiles by lazy {
        hashSetOf(EntityType.ARROW, EntityType.SPECTRAL_ARROW, EntityType.TRIDENT)
    }

    fun Event.call() {
        Bukkit.getPluginManager().callEvent(this)
    }

    fun EntityDamageByEntityEvent.getAttackCooldown(): Double {
        if (this.isProjectileDamage()) return AttributeListener.arrowForce[this.damager.uniqueId]?.toDouble() ?: 1.0

        val damager = this.damager as? LivingEntity ?: return 1.0
        return damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value?.let { value ->
            val force = this.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE) / value
            if (force.isNaN()) 1.0 else force
        }?.format(2) ?: 1.0
    }

    fun EntityDamageByEntityEvent.isProjectileDamage(): Boolean {
        return this.cause == EntityDamageEvent.DamageCause.PROJECTILE && this.damager.type in whiteListProjectiles
    }

    fun EntityDamageByEntityEvent.getAttacker(): LivingEntity? {
        return if (this.isProjectileDamage()) AttributeListener.shooters[damager.uniqueId]!!.getLivingEntity()!! else damager as? LivingEntity
    }

    fun getAllEventClasses(): MutableSet<Class<out Event>> {
        return Reflections("org.bukkit.event").getSubTypesOf(Event::class.java)
    }

}
package me.mkbaka.atziluth.utils

import me.mkbaka.atziluth.internal.module.attributes.AttributeListener
import me.mkbaka.atziluth.utils.EntityUtil.getLivingEntity
import me.mkbaka.atziluth.utils.Util.ifNaN
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.reflections.Reflections
import taboolib.common5.format
import taboolib.module.nms.MinecraftVersion

object EventUtil {

    val whiteListProjectiles by lazy {
        hashSetOf(
            EntityType.ARROW,
            EntityType.SPECTRAL_ARROW,
            EntityType.SNOWBALL,
            EntityType.EGG,
            EntityType.FIREBALL
        ).apply {
            if (MinecraftVersion.isHigherOrEqual(5)) {
                add(EntityType.TRIDENT)
            }
        }
    }

    fun Event.call() {
        Bukkit.getPluginManager().callEvent(this)
    }

    fun EntityDamageByEntityEvent.getAttackCooldown(): Double {
        if (this.isProjectileDamage()) return AttributeListener.arrowForce[this.damager.uniqueId]?.toDouble() ?: 1.0

        val damager = this.damager as? LivingEntity ?: return 1.0

        val originDamage = this.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE)
        if (originDamage <= 0.0) return 1.0

        return damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value?.let { value ->
            val force = originDamage / value
            force.ifNaN { 1.0 }
        }?.format(2) ?: 1.0
    }

    fun EntityDamageByEntityEvent.isProjectileDamage(): Boolean {
        return this.cause == EntityDamageEvent.DamageCause.PROJECTILE && this.damager.type in whiteListProjectiles
    }

    fun EntityDamageByEntityEvent.getAttacker(): LivingEntity? {
        return when {
            isProjectileDamage() -> AttributeListener.shooters[damager.uniqueId]?.getLivingEntity()!!
            else -> damager as? LivingEntity
        }
    }

    fun getAllEventClasses(): MutableSet<Class<out Event>> {
        return Reflections("org.bukkit.event").getSubTypesOf(Event::class.java)
    }

}
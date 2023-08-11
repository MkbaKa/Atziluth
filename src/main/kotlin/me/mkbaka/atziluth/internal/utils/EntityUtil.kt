package me.mkbaka.atziluth.internal.utils

import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

object EntityUtil {

    val UUID.entity: Entity?
        get() = Bukkit.getEntity(this)

    val Entity.isLiving: Boolean
        get() = this is LivingEntity && isAlive

    val LivingEntity.isAlive: Boolean
        get() = !isDead && isValid

    fun UUID.getLivingEntity(): LivingEntity? {
        return if (entity?.isLiving == true) entity as LivingEntity else null
    }

    fun doAttributeDamage(attacker: LivingEntity, entity: LivingEntity, damage: Double, attrs: List<String>, whiteListAttrs: List<String> = emptyList(), isClear: Boolean = false) {
        val source = UUID.randomUUID().toString()
        val action = {
            AttributeBridge.addAttr(attacker, source, attrs)
            callSync {
                entity.damage(damage, attacker)
            }
            AttributeBridge.takeAttr(attacker, source)
        }

        if (isClear) {
            AttributeBridge.clearAttributes(attacker, whiteListAttrs) { action() }
        } else action()
    }

    fun doAttributeDamage(attacker: LivingEntity, entities: Collection<LivingEntity>, damage: Double, attrs: List<String>, whiteListAttrs: List<String> = emptyList(), isClear: Boolean = false) {
        val source = UUID.randomUUID().toString()
        val action = {
            AttributeBridge.addAttr(attacker, source, attrs)
            callSync {
                entities.forEach { entity ->
                    entity.damage(damage, attacker)
                }
            }
            AttributeBridge.takeAttr(attacker, source)
        }

        if (isClear) {
            AttributeBridge.clearAttributes(attacker, whiteListAttrs) { action() }
        } else action()
    }

}
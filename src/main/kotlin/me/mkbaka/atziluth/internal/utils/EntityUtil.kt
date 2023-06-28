package me.mkbaka.atziluth.internal.utils

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

}
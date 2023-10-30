package me.mkbaka.atziluth.utils

import org.bukkit.entity.LivingEntity

object EntityUtil {

    val LivingEntity.isAlive: Boolean
        get() = this.isValid && !this.isDead

}
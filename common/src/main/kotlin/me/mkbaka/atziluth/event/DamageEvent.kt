package me.mkbaka.atziluth.event

import org.bukkit.entity.LivingEntity
import taboolib.platform.type.BukkitProxyEvent

class DamageEvent {

    class Pre(val attacker: LivingEntity, val entity: LivingEntity): BukkitProxyEvent()

    class Post(val attacker: LivingEntity, val entity: LivingEntity): BukkitProxyEvent()

}
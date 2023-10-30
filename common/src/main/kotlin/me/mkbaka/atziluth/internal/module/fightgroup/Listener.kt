package me.mkbaka.atziluth.internal.module.fightgroup

import me.mkbaka.atziluth.internal.configuration.impl.FightGroupComponent
import me.mkbaka.atziluth.utils.EventUtil.getAttackCooldown
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object Listener {

    @SubscribeEvent(EventPriority.MONITOR, ignoreCancelled = false)
    fun damage(evt: EntityDamageByEntityEvent) {
        if (evt.isCancelled) return
        val damager = evt.damager as? LivingEntity ?: return
        val entity = evt.entity as? LivingEntity ?: return

        FightGroupComponent.fightGroups.forEach { (_, group) ->
            evt.damage += group.handle(group.evalContext(evt).also { context ->
                context["event"] = evt
                context["damage"] = evt.finalDamage
                context["damager"] = damager
                context["entity"] = entity
                context["force"] = evt.getAttackCooldown()
            })
        }

    }
}
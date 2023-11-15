package me.mkbaka.atziluth.internal.module.fightgroup

//object Listener {
//
//    @SubscribeEvent(EventPriority.MONITOR, ignoreCancelled = false)
//    fun damage(evt: EntityDamageByEntityEvent) {
//        if (evt.isCancelled) return
//        val damager = evt.damager as? LivingEntity ?: return
//        val entity = evt.entity as? LivingEntity ?: return
//
//        FightGroupComponent.fightGroups.forEach { (_, group) ->
//            evt.damage += group.handle(group.evalContext(evt).also { context ->
//                context["event"] = evt
//                context["damage"] = evt.finalDamage
//                context["damager"] = damager
//                context["entity"] = entity
//                context["force"] = evt.getAttackCooldown()
//            })
//        }
//
//    }
//}
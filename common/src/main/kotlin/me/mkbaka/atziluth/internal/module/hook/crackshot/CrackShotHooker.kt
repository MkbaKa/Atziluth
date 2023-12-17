package me.mkbaka.atziluth.internal.module.hook.crackshot

import com.shampaggon.crackshot.events.WeaponShootEvent
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.attributes.AttributeListener
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener

class CrackShotHooker {

    init {
        if (Atziluth.isInitAttributeHooker()) {
            registerBukkitListener(WeaponShootEvent::class.java, priority = EventPriority.MONITOR) { event ->
                AttributeListener.shooters[event.projectile.uniqueId] = event.player.uniqueId
            }
        }
    }

}
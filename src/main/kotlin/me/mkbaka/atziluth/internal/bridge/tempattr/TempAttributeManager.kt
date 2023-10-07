package me.mkbaka.atziluth.internal.bridge.tempattr

import me.mkbaka.atziluth.Atziluth.attributeBridge
import me.mkbaka.atziluth.internal.utils.callSync
import org.bukkit.event.entity.EntityDeathEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TempAttributeManager {

    val tempAttrs = ConcurrentHashMap<UUID, HashMap<String, PlatformExecutor.PlatformTask>>()

    fun addAttr(uuid: UUID, source: String, attrs: List<String>, tick: Long = 0) {
        callSync {
            attributeBridge.addAttributes(uuid, source, attrs)
            if (tick != 0L) {
                tempAttrs.getOrPut(uuid) { hashMapOf() }.compute(source) { _, task ->
                    task?.cancel()
                    submit(delay = tick) {
                        takeAttr(uuid, source)
                    }
                }
            }
        }
    }

    fun takeAttr(uuid: UUID, source: String) {
        attributeBridge.takeAttribute(uuid, source)
        tempAttrs[uuid]?.get(source)?.cancel()
        tempAttrs[uuid]?.remove(source)
    }

    @SubscribeEvent
    fun death(e: EntityDeathEvent) {
        tempAttrs.remove(e.entity.uniqueId)
    }

}
package me.mkbaka.atziluth.internal.module.tempdatamanager.impl

import me.mkbaka.atziluth.internal.module.tempdatamanager.TempDataManager
import me.mkbaka.atziluth.internal.module.tempdatamanager.data.EntityData
import org.bukkit.Bukkit
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common5.clong
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TempDataManagerImpl : TempDataManager {

    val tempDatas = ConcurrentHashMap<UUID, EntityData>()

    val quitKey by lazy { "Atziluth-PlayerQuit" }
    val requireQuitTime by lazy { 1000 * 60 * 30 }

    lateinit var task: PlatformExecutor.PlatformTask

    override fun getData(uuid: UUID, isPlayer: Boolean): EntityData? {
        return tempDatas[uuid]
    }

    @SubscribeEvent
    fun death(e: EntityDeathEvent) {
        tempDatas.remove(e.entity.uniqueId)
    }

    @SubscribeEvent
    fun quit(e: PlayerQuitEvent) {
        tempDatas[e.player.uniqueId]?.saveData(quitKey, System.currentTimeMillis())
    }

    @SubscribeEvent
    fun join(e: PlayerJoinEvent) {
        tempDatas[e.player.uniqueId] = EntityData(e.player.uniqueId, true)
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        task = submitAsync(period = 20 * 60 * 30) {
            val iter = tempDatas.iterator()
            while (iter.hasNext()) {
                val entry = iter.next()
                if (!entry.value.isPlayer) continue
                val player = Bukkit.getPlayer(entry.key)

                if (player == null && entry.value.hasData(quitKey)) {
                    val quitTime = entry.value.getData(quitKey).clong
                    if (System.currentTimeMillis() - quitTime >= requireQuitTime) {
                        iter.remove()
                    }
                }
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        if (this::task.isInitialized) task.cancel()
    }

}
package me.mkbaka.atziluth.internal.data

import me.mkbaka.atziluth.internal.configuration.ConfigManager
import me.mkbaka.atziluth.internal.data.impl.PlayerData
import org.bukkit.Bukkit
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

/**
 * 临时数据
 */
object TempDataManager {

    private val tempData = ConcurrentHashMap<UUID, PlayerData>()

    private lateinit var clearTask: PlatformExecutor.PlatformTask

    /**
     * 获取玩家数据
     * @param [uuid] uuid
     * @return [PlayerData?]
     */
    fun getPlayerData(uuid: UUID): PlayerData? {
        return tempData[uuid]
    }

    @SubscribeEvent
    fun join(e: PlayerJoinEvent) {
        val uuid = e.player.uniqueId
        if (!tempData.containsKey(uuid)) {
            tempData[uuid] = PlayerData(uuid)
        }
    }

    @SubscribeEvent
    fun quit(e: PlayerQuitEvent) {
        val playerData = getPlayerData(e.player.uniqueId) ?: return
        playerData.saveData("PlayerQuitTime", System.currentTimeMillis())
    }

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        clearTask = submitAsync(period = ConfigManager.checkTaskPeriod) {
            val iterator = tempData.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()

                val player = Bukkit.getPlayer(entry.key)
                if (player != null) continue

                val quitTime = entry.value.fromKey("PlayerQuitTime")
                if (quitTime == null || System.currentTimeMillis() - quitTime.clong >= ConfigManager.tempDataTimeout) iterator.remove()
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        if (this::clearTask.isInitialized) clearTask.cancel()
    }

}
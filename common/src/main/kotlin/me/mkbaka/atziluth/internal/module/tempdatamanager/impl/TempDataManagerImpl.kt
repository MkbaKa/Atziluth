package me.mkbaka.atziluth.internal.module.tempdatamanager.impl

import me.mkbaka.atziluth.internal.configuration.ConfigurationManager
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

    lateinit var task: PlatformExecutor.PlatformTask

    override fun getData(uuid: UUID): EntityData? {
        return tempDatas[uuid]
    }

    /**
     * 实体死亡时清除缓存
     */
    @SubscribeEvent
    fun death(e: EntityDeathEvent) {
        tempDatas.remove(e.entity.uniqueId)
    }

    /**
     * 玩家退出时存储当前时间
     */
    @SubscribeEvent
    fun quit(e: PlayerQuitEvent) {
        tempDatas[e.player.uniqueId]?.saveData(quitKey, System.currentTimeMillis())
    }

    /**
     * 玩家进入时初始化数据
     */
    @SubscribeEvent
    fun join(e: PlayerJoinEvent) {
        tempDatas.computeIfAbsent(e.player.uniqueId) {
            EntityData(e.player.uniqueId, isPlayer = true)
        }
    }

    /**
     * 当玩家离线时间超过配置内的时间时
     * 清除缓存
     */
    @Awake(LifeCycle.ENABLE)
    fun enable() {
        task = submitAsync(period = ConfigurationManager.tempDataChecker) {
            val iter = tempDatas.iterator()
            while (iter.hasNext()) {
                val entry = iter.next()
                if (!entry.value.isPlayer) continue
                val player = Bukkit.getPlayer(entry.key)

                if (player == null && entry.value.hasData(quitKey)) {
                    val quitTime = entry.value.getData(quitKey).clong
                    if (System.currentTimeMillis() - quitTime >= ConfigurationManager.tempDataTimeout) {
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
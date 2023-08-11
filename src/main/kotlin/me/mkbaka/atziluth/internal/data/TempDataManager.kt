package me.mkbaka.atziluth.internal.data

/**
 * 临时数据
 */
object TempDataManager {

//    private val tempData = ConcurrentHashMap<UUID, PlayerData>()
//
//    private lateinit var clearTask: PlatformExecutor.PlatformTask
//
//    /**
//     * 获取玩家数据
//     * @param [uuid] uuid
//     * @return [PlayerData?]
//     */
//    fun getPlayerData(uuid: UUID): PlayerData? {
//        return tempData[uuid]
//    }
//
//    @SubscribeEvent
//    fun join(e: PlayerJoinEvent) {
//        val uuid = e.player.uniqueId
//        if (!tempData.containsKey(uuid)) {
//            tempData[uuid] = PlayerData(uuid)
//        }
//    }
//
//    @SubscribeEvent
//    fun quit(e: PlayerQuitEvent) {
//        val playerData = getPlayerData(e.player.uniqueId) ?: return
//        playerData.saveData("PlayerQuitTime", System.currentTimeMillis())
//    }
//
//    @Awake(LifeCycle.ACTIVE)
//    fun active() {
//        clearTask = submitAsync(period = 20 * 60 * 30) {
//            val iterator = tempData.iterator()
//            while (iterator.hasNext()) {
//                val entry = iterator.next()
//
//                val player = Bukkit.getPlayer(entry.key)
//                if (player != null) continue
//
//                val quitTime = entry.value.fromKey("PlayerQuitTime")
//                if (quitTime == null || quitTime.clong >= 1000 * 60 * 30) iterator.remove()
//            }
//        }
//    }
//
//    @Awake(LifeCycle.DISABLE)
//    fun disable() {
//        if (this::clearTask.isInitialized) clearTask.cancel()
//    }

}
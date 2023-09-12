/**
 * 仅用作存储临时数据
 * 下线时间过长会自动删除
 * 不会持久化存储
 */

const TempDataManager = Packages.me.mkbaka.atziluth.internal.data.TempDataManager.INSTANCE

/**
 * 获取玩家的所有临时数据
 * @param uuid
 * @returns me.mkbaka.atziluth.internal.data.PlayerData
 */
const getPlayerData = function (uuid) {
    return TempDataManager.getPlayerData(uuid)
}

/**
 * 根据指定的 key 从玩家的临时数据里获取存储的值
 * @param player
 * @param key
 * @returns Any?
 */
const getDataFromKey = function (player, key) {
    const data = getPlayerData(player.uniqueId)
    if (data == null) return null
    return data.fromKey(key)
}

/**
 * 判断玩家是否有指定数据
 * @param player
 * @param key
 * @returns boolean
 */
const hasData = function (player, key) {
    const data = getPlayerData(player.uniqueId)
    if (data == null) return false
    return data.hasData(key)
}
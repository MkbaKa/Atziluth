/**
 * 仅用作存储临时数据
 * 下线时间过长会自动删除
 * 不会持久化存储
 */
const TempDataManager = Packages.me.mkbaka.atziluth.Atziluth.INSTANCE.tempDataManager

/**
 * 获取玩家的所有临时数据
 * 数据不存在则返回null
 * @param uuid
 * @returns me.mkbaka.atziluth.internal.module.tempdatamanager.data.EntityData?
 */
const getData = function (uuid) {
    return TempDataManager.getData(uuid)
}

/**
 * 获取实体的所有临时数据
 * 数据不存在则返回null
 * @param uuid 实体的UUID
 * @returns me.mkbaka.atziluth.internal.module.tempdatamanager.data.EntityData?
 */
const getEntityData = function (entity) {
    return TempDataManager.getData(entity.uniqueId)
}

/**
 * 根据指定的 key 从实体的临时数据里获取存储的值
 * 数据不存在则返回null
 * @param uuid 实体的UUID
 * @param key 数据Key
 * @returns Any?
 */
const getDataFromKey = function (uuid, key) {
    const data = getData(uuid)
    if (data == null) return null
    return data.getData(key)
}

/**
 * 根据指定的 key 从实体的临时数据里获取存储的值
 * 数据不存在则返回null
 * @param entity 实体
 * @param key 数据Key
 * @returns Any?
 */
const getEntityDataFromKey = function (entity, key) {
    const data = getData(entity.uniqueId)
    if (data == null) return null
    return data.getData(key)
}

/**
 * 判断uuid指向的数据是否有指定数据
 * 数据不存在则返回false
 * @param entity 实体
 * @param key 数据Key
 * @returns boolean
 */
const hasData = function (uuid, key) {
    const data = getData(uuid)
    if (data == null) return false
    return data.hasData(key)
}

/**
 * 判断实体是否有指定数据
 * 数据不存在则返回false
 * @param entity 实体
 * @param key 数据Key
 * @returns boolean
 */
const entityHasData = function (entity, key) {
    const data = getData(player.uniqueId)
    if (data == null) return false
    return data.hasData(key)
}
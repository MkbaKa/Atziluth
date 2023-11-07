const EntityUtil = Packages.me.mkbaka.atziluth.utils.EntityUtil.INSTANCE

/**
 * 根据 uuid 获取 Entity
 * @param uuid
 * @returns Entity
 */
const getEntity = function (uuid) {
    return EntityUtil.entity(uuid)
}

/**
 * 根据 uuid 获取 LivingEntity
 * @param uuid
 * @returns LivingEntity
 */
const getLivingEntity = function (uuid) {
    return EntityUtil.getLivingEntity(uuid)
}

/**
 * 判断 Entity 是否为 LivingEntity
 * @param entity
 * @returns boolean
 */
const isLiving = function (entity) {
    return EntityUtil.isLiving(entity)
}

/**
 * 获取实体是否存活 (未死亡且非无敌状态)
 * @param entity
 * @returns boolean
 */
const entityIsAlive = function (entity) {
    return EntityUtil.isAlive(entity)
}
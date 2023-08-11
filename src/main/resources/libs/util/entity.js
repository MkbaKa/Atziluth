const EntityUtil = Packages.me.mkbaka.atziluth.internal.utils.EntityUtil.INSTANCE

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

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entity 受击方
 * @param attrs 属性列表 元素的格式为 "属性名: 数值"
 * @param isClear 是否清除玩家身上的属性
 */
const doAttDamage = function (attacker, entity, attrs, isClear) {
    EntityUtil.doAttributeDamage(attacker, entity, attrs, listOf(), isClear)
}

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entity 受击方
 * @param attrs 属性列表 元素的格式为 "属性名: 数值"
 * @param whiteListAttrs 不会被清除的属性名列表
 * @param isClear 是否清除玩家身上的属性
 */
const doAttributeDamage = function (attacker, entity, attrs, whiteListAttrs, isClear) {
    EntityUtil.doAttributeDamage(attacker, entity, attrs, whiteListAttrs, isClear)
}
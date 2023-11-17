/**
 * 判断当前伤害是否由远程攻击造成
 * @param fightData 战斗数据
 * @return Boolean
 */
const isProjectileDamage = function (fightData) {
    return fightData.isProjectileDamage()
}

/**
 * 存储数据
 * 仅在当前 FightData 内有效
 * 同 key 会覆盖旧数据
 * @param fightData 战斗数据
 * @param entity 实体
 * @param key key
 * @param value value
 */
const saveData = function (fightData, entity, key, value) {
    fightData.saveData(entity.uniqueId, key, value)
}

/**
 * 获取数据
 * 只能获取当前 FightData 中的数据
 * 若key不存在则返回null
 * @param fightData 战斗数据
 * @param entity 实体
 * @param key key
 * @return Any?
 */
const getData = function (fightData, entity, key) {
    return fightData.getData(entity.uniqueId, key)
}

/**
 * 获取战斗数据内攻击方的蓄力进度
 * @param fightData 战斗数据
 * @return 0 ~ 1
 */
const getForce = function (fightData) {
    return fightData.getForce()
}

/**
 * 触发指定属性
 * * values内的值会附加给该属性
 * * 触发后删除
 * @param fightData 战斗数据
 * @param attribute 属性名
 * @param values 属性数值
 */
const callAttribute = function (fightData, attribute, values) {
    fightData.callAttribute(attribute, values)
}

/**
 * 获取此次处理时的伤害数值
 * @param fightData 战斗数据
 * @returns Double
 */
const getDamage = function (fightData) {
    return fightData.getDamage()
}

/**
 * 增加造成的伤害数值
 * @param fightData 战斗数据
 * @param value 数值
 */
const addDamage = function (fightData, value) {
    fightData.addDamage(value)
}

/**
 * 减少造成的伤害数值
 * @param fightData 战斗数据
 * @param value 数值
 */
const takeDamage = function (fightData, value) {
    fightData.takeDamage(value)
}

/**
 * 设置造成的伤害数值
 * @param fightData 战斗数据
 * @param value 数值
 */
const setDamage = function (fightData, value) {
    fightData.setDamage(value)
}

/**
 * 增加此次处理中的属性值
 * 仅对当前 FightData 生效
 * 新的攻击不会包含本方法操作的数值
 * @param fightData 战斗数据
 * @param entity 实体
 * @param name 属性名
 * @param value 属性值
 */
const addAttribute = function (fightData, entity, name, value) {
    fightData.addAttribute(entity, name, value)
}

/**
 * 设置此次处理中的属性值
 * 仅对当前 FightData 生效
 * 新的攻击不会包含本方法操作的数值
 * @param fightData 战斗数据
 * @param entity 实体
 * @param name 属性名
 * @param value 属性值
 */
const setAttribute = function (fightData, entity, name, value) {
    fightData.setAttribute(entity, name, value)
}

/**
 * 减少此次处理中的属性值
 * 仅对当前 FightData 生效
 * 新的攻击不会包含本方法操作的数值
 * @param fightData 战斗数据
 * @param entity 实体
 * @param name 属性名
 * @param value 属性值
 */
const takeAttribute = function (fightData, entity, name, value) {
    fightData.takeAttribute(entity, name, value)
}

/**
 * 获取此次处理中的属性值
 * @param fightData 战斗数据
 * @param entity 实体
 * @param name 属性名
 * @param valueType 属性值类型
 * @return Double
 */
const getAttribute = function (fightData, entity, name, valueType) {
    return fightData.getAttribute(entity, name, valueType)
}

/**
 * 获取战斗数据中的属性值
 * @param fightData 战斗数据
 * @param entity 实体
 * @param name 属性名
 * @return DoubleArray
 */
const getAttributeValues = function (fightData, entity, name) {
    return fightData.getAttributeValues(entity, name)
}

/**
 * 获取战斗数据中属性的随机值
 * 等同于 getAttribute(fightData, entity, name, AttributeValueType.RANDOM)
 * @param fightData
 * @param entity
 * @param name
 * @return Double
 */
const getRandomValue = function (fightData, entity, name) {
    return fightData.getRandomValue(entity, name)
}
const AttributeValueType = Packages.me.mkbaka.atziluth.internal.bridge.AttributeValueType

/**
 * 获取运行至当前属性时的伤害数值
 * @param {*} attacker 攻击者
 * @returns
 */
const getDamage = function (attacker) {
    return Attr.getFinalDamage(attacker)
}

/**
 * 增加造成的伤害数值
 * @param {*} attacker 攻击者
 * @param {*} value 数值
 */
const addDamage = function (attacker, value) {
    Attr.addFinalDamage(attacker, value)
}

/**
 * 减少造成的伤害数值
 * @param {*} attacker 攻击者
 * @param {*} value 数值
 */
const takeDamage = function (attacker, value) {
    Attr.takeFinalDamage(attacker, value)
}

/**
 * 设置造成的伤害数值
 * @param {*} attacker 攻击者
 * @param {*} value 数值
 */
const setDamage = function (attacker) {
    Attr.setFinalDamage(attacker)
}

/**
 * 增加属性
 * @param entity 实体
 * @param source 属性源
 * @param attrs 属性列表
 */
const addAttributes = function (entity, source, attrs) {
    bridge.addAttributes(entity, source, attrs)
}

/**
 * 删除属性源内的属性
 * @param entity 实体
 * @param source 属性源
 */
const takeAttribute = function (entity, source) {
    bridge.takeAttribute(entity, source)
}

/**
 * 获取指定属性的最小数值
 * @param entity 实体
 * @param attrName 属性名
 * @return 数值
 */
const getMinValue = function (entity, attrName) {
    return bridge.getAttrValue(entity, attrName, AttributeValueType.MIN)
}

/**
 * 获取指定属性的最大数值
 * @param entity 实体
 * @param attrName 属性名
 * @return 数值
 */
const getMaxValue = function (entity, attrName) {
    return bridge.getAttrValue(entity, attrName, AttributeValueType.MAX)
}

/**
 * 在属性的最小和最大属性值中随机一个数值
 * @param entity 实体
 * @param attrName 属性名
 * @return 数值
 */
const getAttrValue = function (entity, attrName) {
    return bridge.getAttrValue(entity, attrName, AttributeValueType.RANDOM)
}
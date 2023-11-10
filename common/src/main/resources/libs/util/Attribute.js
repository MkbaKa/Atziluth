const AttributeValueType = Packages.me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
const AttributeAPI = Packages.me.mkbaka.atziluth.api.AttributeAPI.INSTANCE
const AttributeHooker = Atziluth.attributeHooker
const TempAttributeData = Packages.me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData.Companion

/**
 * 获取运行至当前属性时的伤害数值
 * @returns double
 */
const getDamage = function () {
    return this.event.finalDamage
}

/**
 * 增加造成的伤害数值
 * @param {*} value 数值
 */
const addDamage = function (value) {
    this.event.damage += value
}

/**
 * 减少造成的伤害数值
 * @param {*} value 数值
 */
const takeDamage = function (value) {
    this.event.damage -= value
}

/**
 * 设置造成的伤害数值
 * @param {*} value 数值
 */
const setDamage = function (value) {
    this.event.damage = value
}

/**
 * 增加属性
 * @param entity 实体
 * @param source 属性源
 * @param attrs 属性列表 Map<String, Array<Double>>
 * @param merge 若同源已存在是否堆叠属性值 否则将替换
 */
const addAttributes = function (entity, source, attrs, merge) {
    addTempAttribute(entity, newTempAttributeData(entity.uniqueId, source, attrs, function (data) {}), merge)
}

/**
 * 增加属性
 * @param entity 实体
 * @param tempAttributeData 属性数据
 * @param merge 若同源已存在是否堆叠属性值 否则将替换
 */
const addTempAttribute = function (entity, tempAttributeData, merge) {
    AttributeAPI.addAttribute(entity, tempAttributeData, merge)
}

/**
 * 删除属性源内的属性
 * @param entity 实体
 * @param source 属性源
 */
const takeAttribute = function (entity, source) {
    AttributeAPI.takeAttribute(entity, source)
}

/**
 * 创建一个临时属性数据
 * @param owner 数据归属者的uuid
 * @param source 属性源
 * @param attrs Map<String, Array<Double>> 即 属性值 对应的 数值
 * @param callback 回调函数
 * @return me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
 */
const newTempAttributeData = function (owner, source, attrs, callback) {
    return TempAttributeData.new(owner, source, attrs, callback)
}

/**
 * 获取指定属性的最小数值
 * @param entity 实体
 * @param attrName 属性名
 * @return 数值
 */
const getMinValue = function (entity, attrName) {
    return AttributeAPI.getAttrValue(entity, attrName, AttributeValueType.MIN)
}

/**
 * 获取指定属性的最大数值
 * @param entity 实体
 * @param attrName 属性名
 * @return 数值
 */
const getMaxValue = function (entity, attrName) {
    return AttributeAPI.getAttrValue(entity, attrName, AttributeValueType.MAX)
}

/**
 * 在属性的最小和最大属性值中随机一个数值
 * @param entity 实体
 * @param attrName 属性名
 * @return 数值
 */
const getAttrValue = function (entity, attrName) {
    return AttributeAPI.getAttrValue(entity, attrName, AttributeValueType.RANDOM)
}

/**
 * 注册属性名
 * @param name 属性名
 * @param combatPower 战斗力
 * @param placeholder 变量
 */
const regOtherAttr = function (name, combatPower, placeholder) {
    AttributeHooker.registerOtherAttribute(name, combatPower, placeholder)
}

/**
 * 设置属性是否跳过过滤即可触发
 * 若为 true 则数值为0也能触发
 * 除 runtime 类型以为 均需要属性插件本身支持才能生效
 * @param value boolean
 */
const skipFilter = function (value) {
    this.Attr.skipFilter = value
}
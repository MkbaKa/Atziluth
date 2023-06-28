const Bukkit = Packages.org.bukkit.Bukkit
const ItemStack = Packages.org.bukkit.inventory.ItemStack
const Material = Packages.org.bukkit.inventory.Material
const Reflex = Packages.me.mkbaka.atziluth.internal.utils.ReflexUtil.INSTANCE

const AttributeValueType = Packages.me.mkbaka.atziluth.internal.bridge.AttributeValueType

const logger = Bukkit.getLogger()

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
 * @return double
 */
const getMinValue = function (entity, attrName) {
    return bridge.getAttrValue(entity, attrName, AttributeValueType.MIN)
}

/**
 * 获取指定属性的最大数值
 * @param entity 实体
 * @param attrName 属性名
 * @return double
 */
const getMaxValue = function (entity, attrName) {
    return bridge.getAttrValue(entity, attrName, AttributeValueType.MAX)
}

/**
 * 在属性的最小和最大属性值中随机一个数值
 * @param entity 实体
 * @param attrName 属性名
 * @return double
 */
const getAttrValue = function (entity, attrName) {
    return bridge.getAttrValue(entity, attrName, AttributeValueType.RANDOM)
}

/**
 * 向后台输出消息
 * @param any
 */
const info = function (any) {
    logger.info(any)
}

/**
 * 向后台输出警告
 * @param any
 */
const warn = function (any) {
    logger.warning(any)
}

/**
 * 向后台输出错误
 * @param any
 */
const error = function (any) {
    logger.severe(any)
}

/**
 * 将js数组转换为ArrayList
 * @param array 数组
 * @returns java.util.ArrayList
 */
const listOf = function (array) {
    const newList = new java.util.ArrayList()
    for (let i in array) {
        newList.add(array[i])
    }
    return newList
}


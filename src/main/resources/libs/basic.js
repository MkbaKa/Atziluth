const Bukkit = Packages.org.bukkit.Bukkit
const ItemStack = Packages.org.bukkit.inventory.ItemStack
const Material = Packages.org.bukkit.Material
const EntityType = Packages.org.bukkit.entity.EntityType
const Reflex = Packages.me.mkbaka.atziluth.internal.utils.ReflexUtil.INSTANCE

const logger = Bukkit.getLogger()

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
const toList = function (array) {
    const newList = new java.util.ArrayList()
    for (let i in array) {
        newList.add(array[i])
    }
    return newList
}

/**
 * 将多个参数变为List集合
 * @param {...any} any 参数列表
 * @returns java.util.ArrayList
 */
const listOf = function () {
    const array = new java.util.ArrayList()
    for (let i in arguments) {
        array.add(arguments[i])
    }
    return array
}

/**
 * 获取随机值
 * @param min 最小数值
 * @param max 最大数值
 * @returns {number}
 */
const random = function (min, max) {
    const minValue = Math.min(min, max)
    const maxValue = Math.max(min, max)
    return Math.random() * (maxValue - minValue) + minValue
}

/**
 * 计算概率
 * @param number 概率值
 * @returns {boolean}
 */
const evalChance = function (number) {
    const chance = number > 1.0 ? number / 100 : number
    return Math.random() <= chance
}

/**
 * 初始化全局变量
 * 省掉this关键字 且支持所有函数内直接调用 不必再挨个传参
 * 如果你不知道该函数有什么作用
 * 请不要 调用 或 修改 甚至是 删除
 */
function initGlobalVariables() {
    globalVariables = this.args
    if (globalVariables !== undefined) {
        Adapter = globalVariables["Attr"]
        if (Adapter !== undefined) Attr = Adapter.inst
        handle = globalVariables["handle"]
        attacker = globalVariables["attacker"]
        entity = globalVariables["entity"]
    }
}
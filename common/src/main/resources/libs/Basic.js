const Bukkit = Packages.org.bukkit.Bukkit
const ItemStack = Packages.org.bukkit.inventory.ItemStack
const Material = Packages.org.bukkit.Material
const EntityType = Packages.org.bukkit.entity.EntityType
const Location = Packages.org.bukkit.Location

const Atziluth = Packages.me.mkbaka.atziluth.Atziluth.INSTANCE
const UUID = java.util.UUID
const HashMap = java.util.HashMap
const HashSet = java.util.HashSet
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
 * 将js数组转换为double[]
 * @returns double[]
 */
const toDoubleArray = function (jsArray) {
    return Java.to(jsArray, "double[]")
}

/**
 * 将多个参数塞进java数组
 * @returns double[]
 */
const newArray = function () {
    const array = []
    for (let i in arguments) {
        array[i] = arguments[i]
    }
    return Java.to(array, "double[]")
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
 * 计算概率 数值会自动 除以100
 * 用于较大的大数字
 * @param number 概率值
 * @returns {boolean}
 */
const evalChance = function (number) {
    return Math.random() <= number / 100
}

/**
 * 计算怪率 数值不会 除以100
 * 用于已知的小数
 * @param number
 * @returns {boolean}
 */
const chance = function (number) {
    return Math.random() <= number
}

/**
 * 随机一段UUID
 * @returns java.util.UUID
 */
const randomUUID = function () {
    return UUID.randomUUID()
}
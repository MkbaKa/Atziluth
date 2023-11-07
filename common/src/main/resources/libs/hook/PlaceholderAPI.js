const PlaceholderAPIHooker = Packages.me.mkbaka.atziluth.Atziluth.hookerManager.placeholderAPIHooker
const ProxyExpansion = Packages.me.mkbaka.atziluth.internal.module.hook.placeholderapi.ProxyExpansion

/**
 * 创建一个PAPI扩展
 * @param identifier 修饰符 即变量开头的字符串
 * @returns me.mkbaka.atziluth.internal.module.hook.placeholderapi.ProxyExpansion
 */
const createExpansion = function (identifier) {
    return new ProxyExpansion(identifier)
}

/**
 * 转换字符串内的 PAPI 变量
 * @param player 玩家
 * @param str 字符串
 * @returns 转换后的字符串
 */
const parse = function (player, str) {
    return PlaceholderAPIHooker.parse(player, str)
}

/**
 * 转换字符串列表
 * @param player 玩家
 * @param strList 列表
 * @returns 转换后的列表
 */
const parseList = function (player, strList) {
    return PlaceholderAPIHooker.parse(player, strList)
}
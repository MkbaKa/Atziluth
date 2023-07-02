const Util = Packages.me.mkbaka.atziluth.internal.utils.Util.INSTANCE
const CompatUtil = Packages.me.mkbaka.atziluth.internal.utils.CompatUtil.INSTANCE

/**
 * 转换文本中的PlaceholderAPI变量
 * 变量目标为自己 前提是上下文内有player这个变量
 * @param str 文本
 * @return 转换后的文本
 */
const papi = function (str) {
    return CompatUtil.parse(player, str)
}

/**
 * 对指定目标转换变量
 * @param target 目标玩家
 * @param str 文本
 * @return 转换后的文本
 */
const parsePapi = function (target, str) {
    return CompatUtil.parse(target, str)
}

/**
 * 判断变量值是否大于等于指定数值
 * @param {*} player 玩家
 * @param {*} str 变量
 * @param {*} target 目标数值
 * @returns
 */
const checkPlaceholder = function (player, str, target) {
    return CompatUtil.check(player, str, target)
}

/**
 * 转换文本内的颜色
 * @param str 文本
 * @return 转换后的文本
 */
const color = function (str) {
    return Util.colored(str)
}

/**
 * 去除文本内的颜色
 * @param str 文本
 * @returns 转换后的文本
 */
const uncolor = function (str) {
    return Util.uncolored(str)
}
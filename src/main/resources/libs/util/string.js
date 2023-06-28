const Util = Packages.me.mkbaka.atziluth.internal.utils.Util.INSTANCE
const CompatUtil = Packages.me.mkbaka.atziluth.internal.utils.CompatUtil.INSTANCE

/**
 * 转换文本中的变量 (变量目标为自己)
 * @param str 文本
 * @return 转换后的文本
 */
const parse = function (str) {
    return CompatUtil.parse(player, str)
}

/**
 * 对指定目标转换变量
 * @param target 目标玩家
 * @param str 文本
 * @return 转换后的文本
 */
const parseOther = function (target, str) {
    return CompatUtil.parse(target, str)
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
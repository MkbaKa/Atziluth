const StringUtil = Packages.me.mkbaka.atziluth.utils.StringUtil.INSTANCE

/**
 * 转换文本内的颜色
 * @param str 文本
 * @return 转换后的文本
 */
const color = function (str) {
    return StringUtil.colored(str)
}

/**
 * 去除文本内的颜色
 * @param str 文本
 * @returns 转换后的文本
 */
const uncolor = function (str) {
    return StringUtil.uncolored(str)
}

/**
 * 转换文本内的颜色
 * @param list 文本列表
 * @return 转换后的文本列表
 */
const colorList = function (list) {
    return StringUtil.colored(list)
}

/**
 * 去除文本内的颜色
 * @param list 文本列表
 * @returns 转换后的文本列表
 */
const uncolorList = function (list) {
    return StringUtil.uncolored(list)
}
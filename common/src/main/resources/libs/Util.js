const ClassUtil = Packages.me.mkbaka.atziluth.utils.ClassUtil.INSTANCE

/**
 * 获取一个类
 * @param className 全限定类名
 */
const find = function (className) {
    return ClassUtil.getClass(className)
}

/**
 * 获取静态类
 * @param className 全限定类目
 */
const static = function (className) {
    return ClassUtil.staticClass(className)
}

/**
 * 判断目标是否拥有指定权限
 * @param perm 权限节点
 * @returns boolean
 */
const hasPerm = function (target, perm) {
    return target.hasPermission(perm)
}
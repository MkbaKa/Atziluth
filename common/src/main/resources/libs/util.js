const ClassUtil = Packages.me.mkbaka.atziluth.internal.utils.ClassUtil.INSTANCE
const SimpleListener = Packages.me.mkbaka.atziluth.internal.listener.SimpleListener
const SchedulerUtil = Packages.me.mkbaka.atziluth.internal.utils.SchedulerUtilKt

/**
 * 创建一个监听器对象
 * @param source 监听器源
 * @return me.mkbaka.atziluth.internal.listener.SimpleListener
 */
const createListener = function (source) {
    return new SimpleListener(source)
}

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
 * 创建同步任务
 * @param callback 执行的代码
 */
const callSync = function (callback) {
    SchedulerUtil.callSync(callback)
}

/**
 * 创建异步任务
 * @param callback 执行的代码
 */
const callAsync = function (callback) {
    SchedulerUtil.callAsync(callback)
}

/**
 * 创建延迟同步任务
 * @param delay 延迟 (ticks)
 * @param callback  执行的代码
 * @returns org.bukkit.scheduler.BukkitTask
 */
const callSyncLater = function (delay, callback) {
    return SchedulerUtil.callSyncLater(delay, callback)
}

/**
 * 创建延迟异步任务
 * @param delay 延迟 (ticks)
 * @param callback  执行的代码
 * @returns org.bukkit.scheduler.BukkitTask
 */
const callAsyncLater = function (delay, callback) {
    return SchedulerUtil.callAsyncLater(delay, callback)
}

/**
 * 创建同步持续任务
 * @param delay 延迟 (ticks)
 * @param period 持续间隔 (ticks)
 * @param callback  执行的代码
 * @returns org.bukkit.scheduler.BukkitTask
 */
const callSyncTimer = function (delay, period, callback) {
    return SchedulerUtil.callSyncTimer(delay, period, callback)
}

/**
 * 创建异步持续任务
 * @param delay 延迟 (ticks)
 * @param period 持续间隔 (ticks)
 * @param callback  执行的代码
 * @returns org.bukkit.scheduler.BukkitTask
 */
const callAsyncTimer = function (delay, period, callback) {
    return SchedulerUtil.callAsyncTimer(delay, period, callback)
}

/**
 * 判断自身是否拥有指定权限
 * 判断的目标为自己 顶级变量内需要有player这个变量
 * @param perm 权限节点
 * @returns boolean
 */
const hasPerm = function (perm) {
    return player.hasPermssion(perm)
}

/**
 * 判断目标是否拥有指定权限
 * @param perm 权限节点
 * @returns boolean
 */
const hasPermssion = function (target, perm) {
    return target.hasPermission(perm)
}
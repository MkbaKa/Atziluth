const Scheduler = Packages.me.mkbaka.atziluth.utils.scriptutil.scheduler.Scheduler
const SchedulerUtil = Packages.me.mkbaka.atziluth.utils.SchedulerUtil.INSTANCE
const SchedulerCompanion = Packages.me.mkbaka.atziluth.utils.scriptutil.scheduler.Scheduler.Companion

/**
 * 创建一个有源调度器
 * @param source 源
 * @returns me.mkbaka.atziluth.utils.scriptutil.scheduler.Scheduler
 */
const createScheduler = function (source) {
    return new Scheduler(source)
}

/**
 * 创建一个有源调度器
 * 和上面一样 只是函数名不同 看个人习惯
 * @param source 源
 * @returns me.mkbaka.atziluth.utils.scriptutil.scheduler.Scheduler
 */
const createTask = function (source) {
    return new Scheduler(source)
}

/**
 * 运行同步代码块
 * @param callback 代码块
 */
const callSync = function (callback) {
    SchedulerUtil.callSync(callback)
}

/**
 * 运行异步代码块
 * @param callback 代码块
 */
const callAsync = function (callback) {
    SchedulerUtil.callAsync(callback)
}

/**
 * 延迟运行同步代码块
 * @param source 源 ( 如果你不知道这里该填什么,可以写 randomUUID().toString() )
 * @param delay 延迟 (tick)
 * @param callback 代码块
 */
const callSyncLater = function (source, delay, callback) {
    createScheduler(source)
        .setDelay(delay)
        .setExecutor(function (time) {
            callback(time)
        })
        .start()
}

/**
 * 延迟运行异步代码块
 * @param source 源 ( 如果你不知道这里该填什么,可以写 randomUUID().toString() )
 * @param delay
 * @param callback
 */
const callAsyncLater = function (source, delay, callback) {
    createScheduler(source)
        .setDelay(delay)
        .setAsync(true)
        .setExecutor(function (time) {
            callback(time)
        })
        .start()
}

/**
 * 定时运行同步代码块
 * @param source 源 ( 如果你不知道这里该填什么,可以写 randomUUID().toString() )
 * @param delay 起始延迟 (tick)
 * @param period 间隔 (tick)
 * @param callback 代码块 (参数为执行时间, 也就是执行的第x秒)
 */
const callSyncTimer = function (source, delay, period, callback) {
    createScheduler(source)
        .setDelay(delay)
        .setPeriod(period)
        .setExecutor(function (time) {
            callback(time)
        })
        .start()
}

/**
 * 定时运行异步代码块
 * @param source 源 ( 如果你不知道这里该填什么,可以写 randomUUID().toString() )
 * @param delay 起始延迟 (tick)
 * @param period 间隔 (tick)
 * @param callback 代码块 (参数为执行时间, 也就是执行的第x秒)
 */
const callAsyncTimer = function (source, delay, period, callback) {
    createScheduler(source)
        .setDelay(delay)
        .setPeriod(period)
        .setAsync(true)
        .setExecutor(function (time) {
            callback(time)
        })
        .start()
}

/**
 * 取消调度器执行
 * @param source 源
 */
const cancelScheduler = function (source) {
    SchedulerCompanion.cancelScheduler(source)
}
const Listener = Packages.me.mkbaka.atziluth.utils.scriptutil.listener.Listener
const ListenerManager = Packages.me.mkbaka.atziluth.utils.scriptutil.listener.ListenerManager.INSTANCE

/**
 * 创建一个有源监听器
 * @param source 源
 * @returns me.mkbaka.atziluth.utils.scriptutil.listener.Listener
 */
const createListener = function (source) {
    return new Listener(source)
}

/**
 * 注销指定源的监听器
 * @param source 源
 */
const unregister = function (source) {
    ListenerManager.unregister(source)
}

/**
 * 监听 org.bukkit.event 下的所有事件
 * @param filter 过滤器
 * @param func 回调函数
 */
const registerAllListener = function (filter, func) {
    ListenerManager.registerAllListener(filter, func)
}
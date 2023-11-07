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
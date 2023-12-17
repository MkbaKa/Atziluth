const MessageUtil = Packages.me.mkbaka.atziluth.utils.MessageUtil.INSTANCE
/**
 * 发送 ActionBar 消息
 * @param sender 发送者
 * @param msg 消息文本
 */
const sendActionBar = function (sender, msg) {
    MessageUtil.sendActionBar(sender, msg)
}

/**
 * 构建一条 Json 消息
 * 可用方法参考: taboolib.module.chat.impl.DefaultComponent
 *
 * @return taboolib.module.chat.impl.DefaultComponent
 */
const buildJson = function () {
    return MessageUtil.buildJson()
}

/**
 * 将 bukkit 的 CommandSender 转为 taboolib ProxyCommandSender
 * 若为 Player 则转换为 ProxyPlayer
 * @param commandSender CommandSender
 * @return taboolib.common.platform.ProxyCommandSender
 */
const adaptCommandSender = function (commandSender) {
    return MessageUtil.adapt(commandSender)
}
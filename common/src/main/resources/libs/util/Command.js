const Command = Packages.me.mkbaka.atziluth.utils.scriptutil.command.Command

/**
 * 创建一个命令
 * 命令归属 Atziluth
 * @param name 命令名
 * @returns me.mkbaka.atziluth.utils.scriptutil.command.Command
 */
const createCommand = function (name) {
    return new Command(name)
}

/**
 * 创建一个命令
 * 命令归属于指定的插件
 * @param name
 * @param plugin
 * @returns me.mkbaka.atziluth.utils.scriptutil.command.Command
 */
const createCommandByPlugin = function (name, plugin) {
    return new Command(name, plugin)
}
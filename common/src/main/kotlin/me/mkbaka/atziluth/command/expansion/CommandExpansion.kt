package me.mkbaka.atziluth.command.expansion

import me.mkbaka.atziluth.Atziluth
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentDynamic
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
import taboolib.platform.util.bukkitPlugin

/**
 * Atziluth v2.0.0
 *  命令: /atziluth [...]
 *  参数:
 *    - script
 *       - eval <script>
 *       - invoke <path> <function>
 *    - reload [<module>]
 *      重载插件.
 */
fun CommandComponent.sendCommandHelper(checkPermissions: Boolean = true) {
    execute<ProxyCommandSender> { sender, context, _ ->
        val command = context.command
        val builder = StringBuilder().apply {
            appendLine()
            appendLine(" ${Atziluth.showName} &fv${bukkitPlugin.description.version}")
            appendLine(" &7命令: &f/${command.name} &8[...]")
            appendLine(" &7参数:")
        }
        var newline = false

        fun check(children: List<CommandComponent>): List<CommandComponent> {
            // 检查权限
            val filterChildren = if (checkPermissions) {
                children.filter { sender.hasPermission(it.permission) }
            } else {
                children
            }
            // 过滤隐藏
            return filterChildren.filter { it !is CommandComponentLiteral || !it.hidden }
        }

        fun space(space: Int): String {
            return (1..space).joinToString("") { " " }
        }

        fun print(compound: CommandComponent, index: Int, size: Int, offset: Int = 3, level: Int = 0, end: Boolean = false, optional: Boolean = false) {
            var option = optional
            var comment = 0
            when (compound) {
                is CommandComponentLiteral -> {
                    if (size == 1) {
                        builder.appendLine("§7- &f${compound.aliases[0]}")
                    } else {
                        newline = true
                        if (level > 1 || end) {
                            builder.appendLine()
                            builder.append(space(level))
                        }
                        builder.append(space(offset))
                        builder.append("§7- &f${compound.aliases[0]}")
                    }
                    option = false
                    comment = compound.aliases[0].length
                }
                is CommandComponentDynamic -> {
                    val value = if (compound.comment.startsWith("@")) {
                        sender.asLangText(compound.comment.substring(1))
                    } else {
                        compound.comment
                    }
                    comment = if (compound.optional || option) {
                        option = true
                        builder.append(" ").append("§8[<$value>]")
                        compound.comment.length + 4
                    } else {
                        builder.append(" ").append("§7<$value>")
                        compound.comment.length + 2
                    }
                }
            }
            if (level > 0) {
                comment += 1
            }
            val checkedChildren = check(compound.children)
            checkedChildren.forEachIndexed { i, children ->
                // 因 literal 产生新的行
                if (newline) {
                    print(children, i, checkedChildren.size, 0, level + comment, end, option)
                } else {
                    val length = if (offset == 8) command.name.length + 1 else comment + 1
                    print(children, i, checkedChildren.size, offset + length, level, end, option)
                }
            }
        }
        val checkedChildren = check(context.commandCompound.children)
        val size = checkedChildren.size
        checkedChildren.forEachIndexed { index, children ->
            print(children, index, size, end = index + 1 == size)
        }
        builder.appendLine()
        builder.lines().forEach {
            sender.sendMessage(it.colored())
        }
    }
}

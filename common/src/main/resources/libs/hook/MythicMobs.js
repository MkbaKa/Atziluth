const MythicMobsHooker = Packages.me.mkbaka.atziluth.Atziluth.hookerManager.mythicMobsHooker
const ProxyScriptMechanic = Packages.me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
const ProxyScriptCondition = Packages.me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptCondition
const ProxyLocationTargeter = Packages.me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter
const ProxyEntityTargeter = Packages.me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyEntityTargeter

/**
 * 创建一个 ProxyScriptMechanic 对象
 * 用于注册 MythicMobs 技能
 * 不要忘记在最后调用 register() 方法
 * @param {...any} 任意数量的字符串
 * @returns me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptMechanic
 */
function createMythicMechanic() {
    return new ProxyScriptMechanic(toList(arguments))
}

/**
 * 创建一个 ProxyScriptCondition 对象
 * 用于注册 MythicMobs 条件
 * 不要忘记在最后调用 register() 方法
 * @param {...any} 任意数量的字符串
 * @returns me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyScriptCondition
 */
function createMythicCondition() {
    return new ProxyScriptCondition(toList(arguments))
}

/**
 * 创建一个 ProxyScriptCondition 对象
 * 用于注册 MythicMobs 坐标类选择器
 * 不要忘记在最后调用 register() 方法
 * @param {...any} 任意数量的字符串
 * @returns me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter
 */
function createMythicLocationTargeter() {
    return new ProxyLocationTargeter(toList(arguments))
}

/**
 * 创建一个 ProxyEntityTargeter 对象
 * 用于注册 MythicMobs 实体类选择器
 * 不要忘记在最后调用 register() 方法
 * @param {...any} 任意数量的字符串
 * @returns me.mkbaka.atziluth.internal.module.hook.mythicmobs.script.ProxyLocationTargeter
 */
function createMythicEntityTargeter() {
    return new ProxyEntityTargeter(toList(arguments))
}

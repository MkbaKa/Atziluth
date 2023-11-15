package me.mkbaka.atziluth.internal.module.script

/**
 * 脚本对象
 */
interface Script {

    /**
     * 获取顶级变量
     * @param [name] 变量名
     * @return [T?]
     */
    fun <T> getTopLevel(name: String): T?

    /**
     * 获取顶级变量
     * @param [name] 变量名
     * @param [def] 若变量不存在返回的值
     * @return [T]
     */
    fun <T> getTopLevel(name: String, def: T): T

    /**
     * 执行字符串脚本
     * @param [args] 参数
     * @return [Any?]
     */
    fun evalScript(args: Map<String, Any>): Any?

    /**
     * 调用脚本函数
     * @param [func] 函数名
     * @param [topLevels] 顶级变量
     * @param [args] 函数参数
     * @return [Any?]
     */
    fun invokeFunction(func: String, topLevels: Map<String, Any> = hashMapOf(), vararg args: Any): Any?

    /**
     * 判断该函数是否存在
     * @param [func] 函数名
     * @return [Boolean]
     */
    fun isFunction(func: String): Boolean

}
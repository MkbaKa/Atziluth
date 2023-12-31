package me.mkbaka.atziluth.internal.scriptreader

import me.mkbaka.atziluth.internal.scriptreader.impl.JavaScriptReaderImpl
import me.mkbaka.atziluth.internal.scriptreader.impl.KetherScriptReaderImpl
import org.bukkit.command.CommandSender
import java.io.File
import java.io.Reader

abstract class AbstractScriptReader {

    protected val script: String

    constructor(script: String) {
        this.script = script
    }

    constructor(reader: Reader) {
        this.script = reader.use { it.readText() }
    }

    /**
     * 获取顶级变量
     * @param [name] 名字
     * @return [T]
     */
    abstract fun <T> getTopLevel(name: String): T?

    /**
     * 获取顶级变量
     * @param [name] 名字
     * @param [def] 默认值
     * @return [T]
     */
    abstract fun <T> getTopLevel(name: String, def: T): T

    /**
     * 执行脚本内容
     * @param [map] 顶级变量
     * @return [Any]
     */
    abstract fun eval(sender: CommandSender?, map: Map<String, Any>): Any?

    /**
     * 调用函数
     * @param [name] 名字
     * @param [map] 顶级变量列表
     * @param [args] 函数参数
     * @return [Any]
     */
    abstract fun invoke(name: String, map: Map<String, Any> = hashMapOf(), vararg args: Any): Any?

    /**
     * 是否为函数
     * @param [name] 名字
     * @return [Boolean]
     */
    abstract fun isFunction(name: String): Boolean

    companion object {

        /**
         * 创建一个脚本读取器
         * @param [file] 文件
         * @return [AbstractScriptReader]
         */
        fun create(file: File): AbstractScriptReader {
            return read(file.extension, file) { file.reader() }
        }

        /**
         * 创建一个脚本读取器
         * @param [scriptType] 脚本类型
         * @param [script] 脚本
         * @return [AbstractScriptReader]
         */
        fun create(scriptType: ScriptType, script: String): AbstractScriptReader {
            return read(scriptType, script) { script.reader() }
        }

        /**
         * 创建一个脚本读取器
         * @param [scriptType] 脚本类型
         * @param [reader] Reader
         * @return [AbstractScriptReader]
         */
        fun create(scriptType: ScriptType, reader: Reader): AbstractScriptReader {
            return read(scriptType, reader) { reader }
        }

        /**
         * 创建一个脚本读取器
         * @param [scriptType] 脚本类型
         * @param [script] 脚本
         * @return [AbstractScriptReader]
         */
        fun create(scriptType: String, script: String): AbstractScriptReader {
            return read(scriptType, script) { script.reader() }
        }

        /**
         * 创建一个脚本读取器
         * @param [scriptType] 脚本类型
         * @param [reader] Reader
         * @return [AbstractScriptReader]
         */
        fun create(scriptType: String, reader: Reader): AbstractScriptReader {
            return read(scriptType, reader) { reader }
        }

        /**
         * 读取
         * @param [scriptType] 脚本类型
         * @param [obj] object
         * @param [script] 脚本
         * @return [AbstractScriptReader]
         */
        private fun <T> read(scriptType: Any, obj: T, script: (T) -> Reader): AbstractScriptReader {
            return when (val type =
                if (scriptType is ScriptType) scriptType else ScriptType.of(scriptType.toString())
            ) {
                ScriptType.JAVASCRIPT -> JavaScriptReaderImpl(script(obj))
                ScriptType.KETHER -> KetherScriptReaderImpl(script(obj))
                else -> error("Unsupported script type $type")
            }
        }

    }

}
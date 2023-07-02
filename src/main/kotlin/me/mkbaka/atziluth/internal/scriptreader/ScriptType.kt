package me.mkbaka.atziluth.internal.scriptreader

/**
 * 脚本类型
 * @param [names] 名字
 */
enum class ScriptType(vararg val names: String) {

    JAVASCRIPT("js"),
    KETHER("ke", "ks", "kether");

    companion object {

        /**
         * 根据Reader名获取一个ScriptType
         * @param [string] Reader名
         * @return [ScriptType?]
         */
        fun of(string: String): ScriptType? {
            return values().firstOrNull { string.lowercase() in it.names }
        }

    }

}
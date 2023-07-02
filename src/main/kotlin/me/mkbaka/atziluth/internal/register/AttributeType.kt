package me.mkbaka.atziluth.internal.register

/**
 * 属性类型
 * @param [function] 执行时的函数名
 */
enum class AttributeType(val function: String) {

    ATTACK("onAttack"),
    DEFENSE("onDefense"),
    RUNTIME("run"),
    OTHER("");

    companion object {

        /**
         * 通过类型名获取一个属性类型
         * @param [string] 字符串
         * @return [AttributeType]
         */
        fun of(string: String): AttributeType? {
            return values().firstOrNull { it.name == string.uppercase() }
        }

    }
}
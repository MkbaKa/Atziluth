package me.mkbaka.atziluth.internal.register

enum class AttributeType(val function: String) {
    ATTACK("onAttack"),
    DEFENSE("onDefense"),
    RUNTIME("run"),
    OTHER("");

    companion object {

        fun of(string: String): AttributeType {
            return valueOf(string.uppercase())
        }

    }
}
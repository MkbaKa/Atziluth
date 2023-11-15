package me.mkbaka.atziluth.internal.module.attributes.attribute

enum class CustomAttributeType(val function: String) {

    ATTACK("onAttack"),
    DEFENSE("onDefense"),
    RUNTIME("run"),
    UPDATE("run"),
    OTHER("");

}
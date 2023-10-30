package me.mkbaka.atziluth.internal.module.attributes.attribute

import taboolib.common5.eqic

enum class CustomAttributeType(val function: String) {

    ATTACK("onAttack"),
    DEFENSE("onDefense"),
    RUNTIME("run"),
    UPDATE("run"),
    OTHER("");

    companion object {

        fun of(str: String): CustomAttributeType? {
            return CustomAttributeType.values().firstOrNull { type -> str.eqic(type.name) }
        }

    }

}
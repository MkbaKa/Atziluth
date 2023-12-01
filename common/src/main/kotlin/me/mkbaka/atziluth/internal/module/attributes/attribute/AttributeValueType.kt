package me.mkbaka.atziluth.internal.module.attributes.attribute

import taboolib.common5.eqic

enum class AttributeValueType {

    MAX, MIN, RANDOM;

    companion object {

        fun of(str: String): AttributeValueType? {
            return values().firstOrNull { it.name.eqic(str) }
        }

    }

}
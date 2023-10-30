package me.mkbaka.atziluth.internal.module.attributes.datamanager

import taboolib.common5.eqic

enum class AttributeValueType {

    MAX, MIN, RANDOM;

    companion object {

        fun of(str: String): AttributeValueType? {
            return AttributeValueType.values().firstOrNull { type -> type.name.eqic(str) }
        }

    }

}
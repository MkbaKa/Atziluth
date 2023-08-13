package me.mkbaka.atziluth.internal.data

import me.mkbaka.atziluth.internal.data.impl.DataImpl

interface Data {

    fun getValue(): Any

    companion object {

        fun toData(value: Any) = DataImpl(value)

    }

}
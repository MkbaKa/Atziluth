package me.mkbaka.atziluth.internal.data

import me.mkbaka.atziluth.internal.data.impl.DataImpl

abstract class Data(open val value: Any) {

    companion object {

        fun toData(value: Any) = DataImpl(value)

    }

}
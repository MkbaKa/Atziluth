package me.mkbaka.atziluth.internal.data.impl

import me.mkbaka.atziluth.internal.data.Data

class DataImpl(private val value: Any) : Data {

    override fun getValue(): Any {
        return value
    }

}
package me.mkbaka.atziluth.utils.map

import java.util.*

class PriorityMap<V> : TreeMap<Int, V>() {

    override fun put(key: Int, value: V): V? {
        var index = key
        while (this.containsKey(index)) {
            ++index
        }
        return super.put(index, value)
    }

}
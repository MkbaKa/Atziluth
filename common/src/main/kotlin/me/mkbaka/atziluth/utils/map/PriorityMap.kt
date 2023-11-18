package me.mkbaka.atziluth.utils.map

import java.util.*

class PriorityMap<V> : TreeMap<Int, V>() {

    override fun put(key: Int, value: V): V? {
        var index = key
        while (containsKey(index)) {
            index++
        }
        return super.put(index, value)
    }


    /**
     * 获取元素所在的位置
     * 若元素不存在则返回-1
     * @param [ele] 元素
     * @return [Int]
     */
    fun findIndex(ele: V): Int {
        if (!containsValue(ele)) return -1
        return entries.first { it.value == ele }.key
    }

}
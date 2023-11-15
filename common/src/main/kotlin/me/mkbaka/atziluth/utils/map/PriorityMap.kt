package me.mkbaka.atziluth.utils.map

import java.util.*

class PriorityMap<V> : TreeMap<Int, V>() {

    override fun put(key: Int, value: V): V? {
        return exchange(value, key)
    }


    /**
     * 获取元素所在的位置
     * 若没有则返回-1
     * @param [ele] 元素
     * @return [Int]
     */
    fun findIndex(ele: V): Int {
        if (!containsValue(ele)) return -1
        return entries.first { it.value == ele }.key
    }

    /**
     * 插入元素
     * 若目标位置已存在元素
     * 则插入下一个位置并将后续元素后移
     * @param [ele] 元素
     * @param [target] 目标位置
     * @return [V?]
     */
    private fun exchange(ele: V, target: Int): V? {
        var index = target
        if (containsKey(index)) {
            if (!containsKey(++index)) return super.put(index, ele)
            exchange(get(index)!!, index + 1)
        }
        return super.put(index, ele)
    }

}
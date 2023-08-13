package me.mkbaka.atziluth.internal.data.impl

import me.mkbaka.atziluth.internal.data.Data
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerData(val owner: UUID) : Data {

    private val data = ConcurrentHashMap<String, Data>()

    override fun getValue(): Any {
        return HashMap(data)
    }

    /**
     * 保存数据
     * @param [key] key
     * @param [value] value
     * @return [Data] 保存后的数据
     */
    fun saveData(key: String, value: Any): Any {
        data[key] = Data.toData(value)
        return value
    }

    /**
     * 获取数据
     * @param [key] 关键
     * @return [Data] Data
     */
    fun fromKey(key: String): Data? {
        return data[key]
    }

    /**
     * 判断指定的key是否存在
     * @param [key] 关键
     * @return [Boolean]
     */
    fun hasData(key: String): Boolean {
        return data.containsKey(key)
    }

    /**
     * 判断指定的value是否存在
     * @param [value] 价值
     * @return [Boolean]
     */
    fun hasData(value: Data): Boolean {
        return data.containsValue(value)
    }
    
}
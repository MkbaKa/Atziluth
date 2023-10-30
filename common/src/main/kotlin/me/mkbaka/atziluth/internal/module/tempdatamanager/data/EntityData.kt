package me.mkbaka.atziluth.internal.module.tempdatamanager.data

import me.mkbaka.atziluth.internal.module.tempdatamanager.TempData
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class EntityData(
    val owner: UUID,
    val isPlayer: Boolean = false
) : TempData<Map<String, Any>> {

    private val data = ConcurrentHashMap<String, Any>()

    override val value: Map<String, Any>
        get() = HashMap(data)

    fun saveData(key: String, value: Any): Any? {
        return data.put(key, value)
    }

    fun getData(key: String): Any? {
        return data[key]
    }

    fun hasData(key: String): Boolean {
        return data.containsKey(key)
    }

    fun hasData(value: Any): Boolean {
        return data.containsValue(value)
    }

    fun removeData(key: String): Any? {
        return data.remove(key)
    }

}
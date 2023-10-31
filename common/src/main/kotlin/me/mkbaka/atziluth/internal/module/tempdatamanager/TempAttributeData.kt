package me.mkbaka.atziluth.internal.module.tempdatamanager

import me.mkbaka.atziluth.internal.module.tempdatamanager.data.TempAttributeDataImpl
import java.util.*

interface TempAttributeData {

    /**
     * 属性归属
     */
    val owner: UUID

    /**
     * 属性源
     */
    val source: String

    /**
     * 属性名与数值
     */
    val attrs: MutableMap<String, Array<Double>>

    /**
     * 超时时间(不存在则为-1)
     */
    var timeout: Long

    /**
     * 是否超时
     */
    val isTimeout: Boolean

    /**
     * 合并该属性源 (包括持续时间)
     */
    fun merge(target: TempAttributeData)

    /**
     * 合并属性值
     */
    fun mergeAttribute(map: Map<String, Array<Double>>)

    /**
     * 格式化属性值
     */
    fun formatAttributes(): List<String>

    companion object {

        fun new(owner: UUID, source: String, attrs: MutableMap<String, Array<Double>>, constructor: TempAttributeData.() -> Unit = {}): TempAttributeData {
            return TempAttributeDataImpl(owner, source, attrs).also(constructor)
        }

    }

}
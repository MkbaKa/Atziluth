package me.mkbaka.atziluth.internal.module.tempdatamanager.data

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.Util.append
import me.mkbaka.atziluth.utils.Util.getOrDef
import java.util.*

open class TempAttributeDataImpl(
    override val owner: UUID,
    override val source: String,
    override val attrs: MutableMap<String, DoubleArray> = hashMapOf(),
    override var timeout: Long = -1
) : TempAttributeData {

    private val startTime = System.currentTimeMillis()

    override val formattedStr = mutableListOf<String>()

    override val isTimeout: Boolean
        get() = timeout >= 0 && System.currentTimeMillis() >= startTime + (timeout / 20) * 1000

    override fun merge(target: TempAttributeData) {
        callUpdate {
            mergeAttribute(target.attrs)
            this.formattedStr.addAll(target.formattedStr)
            this.timeout += target.timeout
        }
    }

    override fun mergeAttribute(map: Map<String, DoubleArray>) {
        map.forEach { (name, newArray) ->
            this.attrs.compute(name) { _, oldArray ->
                (oldArray ?: doubleArrayOf(0.0, 0.0)).append(newArray)
            }
        }
    }

    override fun formatAttributes(): List<String> {
        return mutableListOf<String>().also {
            if (formattedStr.isNotEmpty()) it.addAll(formattedStr)
            attrs.forEach { (key, values) ->
                val min = values.getOrDef(0, 0.0)
                it.add("$key: $min - ${values.getOrDef(1, min)}")
            }
        }
    }

    protected fun callUpdate(callback: () -> Unit) {
        Atziluth.attributeHooker.attributeDataManager.takeAttribute(this.owner, this.source)
        callback()
        Atziluth.attributeHooker.attributeDataManager.addAttribute(this.owner, this)
    }

}
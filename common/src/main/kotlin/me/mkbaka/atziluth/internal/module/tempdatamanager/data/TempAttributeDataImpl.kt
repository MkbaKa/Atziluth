package me.mkbaka.atziluth.internal.module.tempdatamanager.data

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.AttributeUtil.append
import me.mkbaka.atziluth.utils.AttributeUtil.getOrDef
import java.util.*

open class TempAttributeDataImpl(
    override val owner: UUID,
    override val source: String,
    override val attrs: MutableMap<String, DoubleArray>,
    override var timeout: Long = -1
) : TempAttributeData {

    private val startTime = System.currentTimeMillis()

    override val isTimeout: Boolean
        get() = System.currentTimeMillis() >= startTime + (timeout / 20) * 1000

    override fun merge(target: TempAttributeData) {
        mergeAttribute(target.attrs)
        this.timeout += target.timeout
    }

    override fun mergeAttribute(map: Map<String, DoubleArray>) {
        callUpdate {
            map.forEach { (name, newArray) ->
                this.attrs.compute(name) { _, oldArray ->
                    (oldArray ?: doubleArrayOf()).append(newArray)
                }
            }
        }
    }

    override fun formatAttributes(): List<String> {
        return this.attrs.map { entry ->
            if (entry.value.isNotEmpty()) "${entry.key}: ${entry.value.getOrDef(0, 0.0)} - ${entry.value.getOrDef(1, 0.0)}" else ""
        }
    }

    protected fun callUpdate(callback: () -> Unit) {
        Atziluth.attributeHooker.attributeDataManager.takeAttribute(this.owner, this.source)
        callback()
        Atziluth.attributeHooker.attributeDataManager.addAttribute(this.owner, this)
    }

}
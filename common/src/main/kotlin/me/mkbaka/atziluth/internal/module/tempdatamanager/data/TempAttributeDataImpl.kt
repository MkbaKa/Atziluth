package me.mkbaka.atziluth.internal.module.tempdatamanager.data

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import java.util.*

open class TempAttributeDataImpl(
    override val owner: UUID,
    override val source: String,
    override val attrs: HashMap<String, Array<Double>>,
    override var timeout: Long = -1
) : TempAttributeData {

    private val startTime = System.currentTimeMillis()

    override val isTimeout: Boolean
        get() = System.currentTimeMillis() >= startTime + (timeout / 20) * 1000

    override fun merge(target: TempAttributeData) {
        mergeAttribute(target.attrs)
        this.timeout += target.timeout
    }

    override fun mergeAttribute(map: Map<String, Array<Double>>) {
        callUpdate {
            map.forEach { (name, newArray) ->
                newArray.forEachIndexed { index, value ->
                    this.attrs[name]?.set(index, value)
                }
            }
        }
    }

    override fun formatAttributes(): List<String> {
        return this.attrs.map { entry -> "${entry.key}: ${entry.value[0]} - ${entry.value.getOrElse(1) { 0 }}" }
    }

    protected fun callUpdate(callback: () -> Unit) {
        Atziluth.attributeHooker.attributeDataManager.takeAttribute(this.owner, this.source)
        callback()
        Atziluth.attributeHooker.attributeDataManager.addAttribute(this.owner, this)
    }

}
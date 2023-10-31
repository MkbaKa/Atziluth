package me.mkbaka.atziluth.internal.module.tempdatamanager.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeDataManager
import org.bukkit.entity.LivingEntity
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TempAttributeDataManagerImpl : TempAttributeDataManager {

    private lateinit var task: PlatformExecutor.PlatformTask
    private val tempAttributeData = ConcurrentHashMap<UUID, HashMap<String, TempAttributeData>>()
    private val attributeDataManager by lazy { Atziluth.attributeHooker.attributeDataManager }

    override fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData, merge: Boolean) {
        addAttribute(entity.uniqueId, tempAttributeData, merge)
    }

    override fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData, merge: Boolean) {
        val map = this.tempAttributeData.getOrPut(uuid) { hashMapOf() }
        map.compute(tempAttributeData.source) { _, oldValue ->
            if (merge && map.containsKey(tempAttributeData.source)) {
                oldValue!!.also { it.mergeAttribute(tempAttributeData.attrs) }
            } else {
                oldValue?.let { attributeDataManager.takeAttribute(uuid, oldValue.source) }
                tempAttributeData.apply { attributeDataManager.addAttribute(uuid, this) }
            }
        }
    }

    override fun takeAttribute(entity: LivingEntity, source: String) {
        takeAttribute(entity.uniqueId, source)
    }

    override fun takeAttribute(uuid: UUID, source: String) {
        val map = tempAttributeData[uuid] ?: return
        if (map.containsKey(source)) {
            attributeDataManager.takeAttribute(uuid, source)
            map.remove(source)
        }
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        task = submitAsync(period = 5) {
            tempAttributeData.forEach { (uuid, map) ->
                map.forEach { (source, data) ->
                    if (data.isTimeout) {
                        attributeDataManager.takeAttribute(uuid, source)
                        map.remove(source)
                    }
                }
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        if (this::task.isInitialized) task.cancel()
    }

}
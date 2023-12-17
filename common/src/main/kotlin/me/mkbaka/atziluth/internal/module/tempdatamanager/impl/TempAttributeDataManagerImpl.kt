package me.mkbaka.atziluth.internal.module.tempdatamanager.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.configuration.ConfigurationManager
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

    /**
     * 检测调度器
     */
    private lateinit var task: PlatformExecutor.PlatformTask

    /**
     * 已增加的临时属性数据
     */
    private val tempAttributeData = ConcurrentHashMap<UUID, HashMap<String, TempAttributeData>>()

    /**
     * 被寄生属性插件的数据操作
     */
    private val attributeDataManager by lazy { Atziluth.attributeHooker.attributeDataManager }

    /**
     * 增加临时属性
     * @param [entity] 实体
     * @param [tempAttributeData] 临时属性数据
     * @param [merge] 同源是否合并属性值
     */
    override fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData, merge: Boolean) {
        addAttribute(entity.uniqueId, tempAttributeData, merge)
    }

    /**
     * 增加临时属性
     * @param [uuid] uuid
     * @param [tempAttributeData] 临时属性数据
     * @param [merge] 同源是否合并属性值
     */
    override fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData, merge: Boolean) {
        val map = this.tempAttributeData.getOrPut(uuid) { hashMapOf() }
        map.compute(tempAttributeData.source) { _, oldData ->
            if (merge && map.containsKey(tempAttributeData.source)) {
                oldData!!.also { it.merge(tempAttributeData) }
            } else {
                oldData?.let { attributeDataManager.takeAttribute(uuid, oldData.source) }
                tempAttributeData.apply { attributeDataManager.addAttribute(uuid, this) }
            }
        }
    }

    /**
     * 删除临时属性
     * @param [entity] 实体
     * @param [source] 属性源
     */
    override fun takeAttribute(entity: LivingEntity, source: String) {
        takeAttribute(entity.uniqueId, source)
    }

    /**
     * 删除临时属性
     * @param [uuid] uuid
     * @param [source] 属性源
     */
    override fun takeAttribute(uuid: UUID, source: String) {
        val map = tempAttributeData[uuid] ?: return
        if (map.containsKey(source)) {
            attributeDataManager.takeAttribute(uuid, source)
            map.remove(source)
        }
    }

    /**
     * 清理已超时的临时属性
     */
    @Awake(LifeCycle.ENABLE)
    fun enable() {
        task = submitAsync(period = ConfigurationManager.tempAttributeChecker) {
            // 能省一点是一点
            if (tempAttributeData.isEmpty()) return@submitAsync
            tempAttributeData.forEach { (uuid, map) ->
                // 能省一点是一点
                if (map.isEmpty()) return@forEach
                map.forEach { (source, data) ->
                    // 若临时属性已超时
                    // 则清除被寄生属性插件内的属性并清除缓存
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
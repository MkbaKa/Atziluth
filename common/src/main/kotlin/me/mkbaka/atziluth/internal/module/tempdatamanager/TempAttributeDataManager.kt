package me.mkbaka.atziluth.internal.module.tempdatamanager

import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * 临时属性管理
 */
interface TempAttributeDataManager {


    /**
     * 增加临时属性
     * @param [entity] 实体
     * @param [tempAttributeData] 临时属性数据
     * @param [merge] 同源是否合并属性值
     */
    fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData, merge: Boolean)

    /**
     * 增加临时属性
     * @param [uuid] uuid
     * @param [tempAttributeData] 临时属性数据
     * @param [merge] 同源是否合并属性值
     */
    fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData, merge: Boolean)

    /**
     * 删除临时属性
     * @param [entity] 实体
     * @param [source] 属性源
     */
    fun takeAttribute(entity: LivingEntity, source: String)

    /**
     * 删除临时属性
     * @param [uuid] uuid
     * @param [source] 属性源
     */
    fun takeAttribute(uuid: UUID, source: String)

}
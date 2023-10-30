package me.mkbaka.atziluth.internal.module.tempdatamanager

import org.bukkit.entity.LivingEntity
import java.util.*

interface TempAttributeDataManager {

    fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData)

    fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData)

    fun mergeAttribute(uuid: UUID, source: String, attrs: Map<String, Array<Double>>)

    fun takeAttribute(entity: LivingEntity, source: String)

    fun takeAttribute(uuid: UUID, source: String)

}
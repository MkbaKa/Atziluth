package me.mkbaka.atziluth.internal.module.tempdatamanager

import org.bukkit.entity.LivingEntity
import java.util.*

interface TempAttributeDataManager {

    fun addAttribute(entity: LivingEntity, tempAttributeData: TempAttributeData, merge: Boolean)

    fun addAttribute(uuid: UUID, tempAttributeData: TempAttributeData, merge: Boolean)

    fun takeAttribute(entity: LivingEntity, source: String)

    fun takeAttribute(uuid: UUID, source: String)

}
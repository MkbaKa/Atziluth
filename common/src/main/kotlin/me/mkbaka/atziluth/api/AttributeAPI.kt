package me.mkbaka.atziluth.api

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import java.util.*

object AttributeAPI {

    private val dataManager by lazy { Atziluth.attributeHooker.attributeDataManager }

    fun UUID.getAttrValue(name: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
        return dataManager.getAttributeValue(this, name, type)
    }

    fun LivingEntity.getAttrValue(name: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
        return dataManager.getAttributeValue(this, name, type)
    }

    fun UUID.addAttribute(tempAttributeData: TempAttributeData) {
        dataManager.addAttribute(this, tempAttributeData)
    }

    fun LivingEntity.addAttribute(tempAttributeData: TempAttributeData) {
        dataManager.addAttribute(this, tempAttributeData)
    }

    fun UUID.takeAttribute(source: String) {
        dataManager.takeAttribute(this, source)
    }

    fun LivingEntity.takeAttribute(source: String) {
        dataManager.takeAttribute(this, source)
    }

}
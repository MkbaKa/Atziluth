package me.mkbaka.atziluth.internal.bridge

import me.mkbaka.atziluth.Atziluth.attributeBridge
import me.mkbaka.atziluth.internal.utils.callSync
import me.mkbaka.atziluth.internal.utils.callSyncLater
import org.bukkit.entity.LivingEntity
import java.util.*

interface AttributeBridge {

    fun addAttributes(uuid: UUID, source: String, attrs: List<String>)

    fun addAttributes(entity: LivingEntity, source: String, attrs: List<String>)

    fun takeAttribute(uuid: UUID, source: String)

    fun takeAttribute(entity: LivingEntity, source: String)

    fun getAttrValue(uuid: UUID, attrName: String, type: AttributeValueType = AttributeValueType.RANDOM): Double

    fun getAttrValue(
        entity: LivingEntity,
        attrName: String,
        type: AttributeValueType = AttributeValueType.RANDOM
    ): Double

    companion object {

        fun addAttr(uuid: UUID, source: String, attrs: List<String>, timeout: Long = 0) {
            callSync {
                attributeBridge.addAttributes(uuid, source, attrs)
                if (timeout != 0L) callSyncLater(timeout) { takeAttr(uuid, source) }
            }
        }

        fun addAttr(entity: LivingEntity, source: String, attrs: List<String>, timeout: Long = 0) {
            callSync {
                attributeBridge.addAttributes(entity, source, attrs)
                if (timeout != 0L) callSyncLater(timeout) { takeAttr(entity, source) }
            }
        }

        fun takeAttr(uuid: UUID, source: String) {
            callSync { attributeBridge.takeAttribute(uuid, source) }
        }

        fun takeAttr(entity: LivingEntity, source: String) {
            callSync { attributeBridge.takeAttribute(entity, source) }
        }

        fun getAttrValue(uuid: UUID, attr: String, type: AttributeValueType = AttributeValueType.RANDOM): Double {
            return attributeBridge.getAttrValue(uuid, attr, type)
        }

        fun getAttrValue(
            entity: LivingEntity,
            attr: String,
            type: AttributeValueType = AttributeValueType.RANDOM
        ): Double {
            return attributeBridge.getAttrValue(entity, attr, type)
        }

    }

}

enum class AttributeValueType {
    MIN, MAX, RANDOM;

    companion object {

        fun of(str: String): AttributeValueType? {
            return values().firstOrNull { str.equals(str, ignoreCase = true) }
        }

    }
}
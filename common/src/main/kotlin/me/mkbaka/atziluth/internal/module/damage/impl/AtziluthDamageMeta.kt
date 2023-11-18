package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.api.AttributeAPI.addAttribute
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.api.AttributeAPI.takeAttribute
import me.mkbaka.atziluth.internal.configuration.ConfigurationManager
import me.mkbaka.atziluth.internal.module.attributes.attribute.AttributeValueType
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.internal.module.tempdatamanager.data.TempAttributeDataImpl
import org.bukkit.entity.LivingEntity
import java.util.*
import kotlin.math.abs

open class AtziluthDamageMeta(
    damager: LivingEntity,
    entities: Collection<LivingEntity>,
    override val options: AtziluthDamageOptions
) : VanillaDamageMeta(damager, entities, options) {

    override fun doDamage() {
        val source = UUID.randomUUID().toString()

        val action = {
            this.damager.addAttribute(TempAttributeData.new(damager.uniqueId, source, options.attributes))
            super.doDamage()
            this.damager.takeAttribute(source)
        }

        when {
            options.clearAttribute -> clearAttribute(action)
            else -> action()
        }

    }

    open fun clearAttribute(callback: () -> Unit) {
        val source = UUID.randomUUID().toString()

        val attrs = filterValues(Atziluth.attributeHooker.getAllAttributes(options.whiteListAttribute))

        damager.addAttribute(TempAttributeDataImpl(damager.uniqueId, source, attrs))
        callback()
        damager.takeAttribute(source)
    }

    protected fun filterValues(keys: Collection<String>): HashMap<String, DoubleArray> {
        val map = hashMapOf<String, DoubleArray>()
        keys.forEach { key ->
            if (key in options.whiteListAttribute || key in ConfigurationManager.clearAttributeWhiteList) return@forEach
            val minValue = damager.getAttrValue(key, AttributeValueType.MIN)
            if (minValue == 0.0) return@forEach

            val maxValue = damager.getAttrValue(key, AttributeValueType.MAX)

            when {
                maxValue > 0.0 -> map[key] = doubleArrayOf(-minValue, -maxValue)
                else -> map[key] = doubleArrayOf(abs(minValue), abs(maxValue))
            }
        }
        return map
    }

}
package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.api.AttributeAPI.addAttribute
import me.mkbaka.atziluth.api.AttributeAPI.getAttrValue
import me.mkbaka.atziluth.api.AttributeAPI.takeAttribute
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.datamanager.AttributeValueType
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
        this.damager.addAttribute(TempAttributeData.new(damager.uniqueId, source, options.attributes))

        when {
            options.clearAttribute -> clearAttribute { super.doDamage() }
            else -> super.doDamage()
        }

        this.damager.takeAttribute(source)
    }

    open fun clearAttribute(callback: () -> Unit) {
        val source = UUID.randomUUID().toString()
        val attrs = hashMapOf<String, Array<Double>>()
        AttributeManagerComponent.attributes.forEach { (_, attr) ->
            val minValue = damager.getAttrValue(attr.attributeName, AttributeValueType.MIN)
            if (minValue == 0.0) return

            val maxValue = damager.getAttrValue(attr.attributeName, AttributeValueType.MAX)

            when {
                maxValue > 0.0 -> attrs[attr.attributeName] = arrayOf(-minValue, -maxValue)
                else -> attrs[attr.attributeName] = arrayOf(abs(minValue), abs(maxValue))
            }

        }

        damager.addAttribute(TempAttributeDataImpl(damager.uniqueId, source, attrs))
        callback()
        damager.takeAttribute(source)
    }

}
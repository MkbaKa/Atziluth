package me.mkbaka.atziluth.internal.module.damage.impl

import me.mkbaka.atziluth.api.AttributeAPI.addAttribute
import me.mkbaka.atziluth.api.AttributeAPI.clearAttribute
import me.mkbaka.atziluth.api.AttributeAPI.takeAttribute
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.SchedulerUtil.callSync
import org.bukkit.entity.LivingEntity
import java.util.*

open class AtziluthDamageMeta(
    damager: LivingEntity,
    entities: Collection<LivingEntity>,
    override val options: AtziluthDamageOptions
) : VanillaDamageMeta(damager, entities, options) {

    override fun doDamage() {
        val source = UUID.randomUUID().toString()

        val action = {
            callSync {
                this.damager.addAttribute(TempAttributeData.new(damager.uniqueId, source, options.attributes))
                super.doDamage()
                this.damager.takeAttribute(source)
            }
        }

        when {
            options.clearAttribute -> this.damager.clearAttribute(options.whiteListAttribute, action)
            else -> action()
        }

    }

}
package me.mkbaka.atziluth.internal.utils.damage.impl

import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.utils.callSync
import me.mkbaka.atziluth.internal.utils.damage.DamageMeta
import me.mkbaka.atziluth.internal.utils.damage.DamageOptions
import org.bukkit.entity.LivingEntity
import java.util.*

class AttributeDamageMeta(
    damager: LivingEntity,
    defenders: Collection<LivingEntity>,
    options: DamageOptions
) : BasicDamageMeta(damager, defenders, options) {

    override fun doDamage() {
        if (damageOptions !is AttributeDamageOptions) return super.doDamage()

        val source = UUID.randomUUID().toString()
        val action = {
            AttributeBridge.addAttr(damager, source, damageOptions.attributes)
            callSync {
                super.doDamage()
            }
            AttributeBridge.takeAttr(damager, source)
        }

        if (damageOptions.clear) {
            AttributeBridge.clearAttributes(damager, damageOptions.whitelistAttributes) { action() }
        } else {
            action()
        }

    }

    companion object {

        fun createMeta(damager: LivingEntity, defender: LivingEntity, options: DamageOptions): DamageMeta {
            return AttributeDamageMeta(damager, listOf(defender), options)
        }

        fun createMeta(damager: LivingEntity, defenders: Collection<LivingEntity>, options: DamageOptions): DamageMeta {
            return AttributeDamageMeta(damager, defenders, options)
        }

    }

}
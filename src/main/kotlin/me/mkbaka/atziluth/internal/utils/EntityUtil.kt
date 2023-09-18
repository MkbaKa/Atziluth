package me.mkbaka.atziluth.internal.utils

import me.mkbaka.atziluth.internal.utils.damage.DamageOptions
import me.mkbaka.atziluth.internal.utils.damage.impl.AttributeDamageMeta
import me.mkbaka.atziluth.internal.utils.damage.impl.AttributeDamageOptions
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

object EntityUtil {

    val UUID.entity: Entity?
        get() = Bukkit.getEntity(this)

    val Entity.isLiving: Boolean
        get() = this is LivingEntity && isAlive

    val LivingEntity.isAlive: Boolean
        get() = !isDead && isValid

    fun UUID.getLivingEntity(): LivingEntity? {
        return if (entity?.isLiving == true) entity as LivingEntity else null
    }

    fun doAttributeDamage(
        attacker: LivingEntity,
        entity: LivingEntity,
        damage: Double,
        attrs: List<String>,
        whiteListAttrs: List<String> = emptyList(),
        isClear: Boolean = false
    ) {
        doAttributeDamage(attacker, listOf(entity), damage, attrs, whiteListAttrs, isClear)
    }

    fun doAttributeDamage(
        attacker: LivingEntity,
        entities: Collection<LivingEntity>,
        damage: Double,
        attrs: List<String>,
        whiteListAttrs: List<String> = emptyList(),
        isClear: Boolean = false
    ) {
        AttributeDamageMeta.createMeta(attacker, entities, AttributeDamageOptions.new {
            this.basicDamageValue = damage
            this.setAttribute(attrs)
            this.setWhitelistAttr(whiteListAttrs)
            this.isClear = isClear
        }).doDamage()
    }

    /**
     * 基于 DamageOptions 造成有源伤害
     * @param [attacker] 攻击方
     * @param [entity] 受击方
     * @param [options] DamageOptions
     */
    fun doDamage(attacker: LivingEntity, entity: LivingEntity, options: DamageOptions) {
        AttributeDamageMeta.createMeta(attacker, entity, options).doDamage()
    }

    /**
     * 基于 DamageOptions 造成有源伤害
     * @param [attacker] 攻击方
     * @param [entities] 受击方
     * @param [options] DamageOptions
     */
    fun doDamage(attacker: LivingEntity, entities: Collection<LivingEntity>, options: DamageOptions) {
        AttributeDamageMeta.createMeta(attacker, entities, options).doDamage()
    }

}
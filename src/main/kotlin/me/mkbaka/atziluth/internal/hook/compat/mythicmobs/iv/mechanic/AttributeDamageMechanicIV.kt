package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.mechanic

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.internal.data.TempDataManager
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillMechanicIV
import me.mkbaka.atziluth.internal.utils.EntityUtil
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import me.mkbaka.atziluth.internal.utils.damage.impl.AttributeDamageOptions
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common5.cbool
import taboolib.common5.cdouble
import taboolib.common5.cint

@CustomSkillMechanic(["attr-damage", "attrdamage"], MythicMobVersion.IV)
class AttributeDamageMechanicIV(
    mlc: MythicLineConfig
) : CustomSkillMechanicIV(mlc) {

    private val damageValue = mlc.getPlaceholderString(arrayOf("damage", "d"), "1.0")
    private val clear = mlc.getPlaceholderString(arrayOf("isClear", "clear", "c"), "false")
    private val noDamageTick = mlc.getPlaceholderString(arrayOf("noDamageTicks", "ndt"), "0")

    private val ignoreImmunity = mlc.getPlaceholderString(arrayOf("ignoreImmunity", "pi", "ii"), "false")
    private val preventKnockback = mlc.getPlaceholderString(arrayOf("preventKnockback", "pk", "pkb"), "false")

    private val entries = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

    /**
     * attr-damage{物理伤害=100;clear=true} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false
        val caster = meta.caster.entity.bukkitEntity as? LivingEntity ?: return false
        if (entity.isAlive && caster.isAlive) {
            val attrs = entries.map { PlaceholderString.of("${it.key}: ${it.value}").get(meta) }

            if (caster is Player) TempDataManager.getPlayerData(caster.uniqueId)
                ?.saveData(AbstractMythicMobsHooker.damageMetadataKey, entries)

            EntityUtil.doDamage(caster, entity, AttributeDamageOptions.new {
                basicDamageValue = damageValue.get(meta).cdouble
                setAttribute(attrs)
                noDamageTicks = noDamageTick.get(meta).cint
                isClear = clear.get(meta).cbool
                isIgnoreImmunity = ignoreImmunity.get(meta).cbool
                isPreventKnockback = preventKnockback.get(meta).cbool
            })

            if (caster is Player) TempDataManager.getPlayerData(caster.uniqueId)
                ?.removeData(AbstractMythicMobsHooker.damageMetadataKey)
        }
        return true
    }

}
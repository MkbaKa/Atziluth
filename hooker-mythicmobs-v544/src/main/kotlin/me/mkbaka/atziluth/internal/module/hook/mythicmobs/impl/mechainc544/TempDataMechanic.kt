package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["settempdata", "set-temp-data", "set-tempdata"])
class TempDataMechanic(
    cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    mlc: MythicLineConfig
) : CustomMechanic(cm, mlc) {

    private val dataKey = mlc.getPlaceholderString(arrayOf("key", "k"), "")
    private val dataValue = mlc.getPlaceholderString(arrayOf("value", "v"), "")

    /**
     * settempdata{key=xxx;value=xxx} @Self
     */
    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
        Atziluth.tempDataManager.getData(caster.uniqueId)?.saveData(dataKey.get(meta), dataValue.get(meta))
        return SkillResult.SUCCESS
    }

}
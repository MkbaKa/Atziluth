package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["settempdata", "set-temp-data", "set-tempdata"])
class TempDataMechanic(mlc: MythicLineConfig) : CustomMechanic(mlc) {

    private val dataKey = mlc.getPlaceholderString(arrayOf("key", "k"), "")
    private val dataValue = mlc.getPlaceholderString(arrayOf("value", "v"), "")

    /**
     * settempdata{key=xxx;value=xxx} @Self
     */
    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): Boolean {
        return Atziluth.tempDataManager.getData(caster.uniqueId)
            ?.saveData(dataKey.get(meta), dataValue.get(meta)) != null
    }

}
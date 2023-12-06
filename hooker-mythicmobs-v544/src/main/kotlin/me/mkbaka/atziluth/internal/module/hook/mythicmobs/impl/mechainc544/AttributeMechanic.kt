package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.clong
import java.util.*

class AttributeMechanic {

    @MythicAnnotations.SkillMechanic(["add-attr", "addattr", "attr-add", "attradd"])
    class Add(
        cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
        mlc: MythicLineConfig
    ) : CustomMechanic(cm, mlc) {

        private val source = mlc.getPlaceholderString(arrayOf("source", "s"), UUID.randomUUID().toString())
        private val timeout = mlc.getPlaceholderString(arrayOf("timeout", "time", "t"), "-1")
        private val merge = mlc.getPlaceholderString(arrayOf("merge", "m"), "false")

        override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
            val attributeSource = source.get(meta)
            val attrs = parseToAttribute(meta)

            Atziluth.tempAttributeDataManager.addAttribute(
                entity, TempAttributeData.new(entity.uniqueId, attributeSource, attrs) {
                    this.timeout = this@Add.timeout.get(meta).clong
                }, merge.get(meta).cbool
            )
            return SkillResult.SUCCESS
        }

    }

    @MythicAnnotations.SkillMechanic(["take-attr", "takeattr", "attr-take", "attrtake"])
    class Take(
        cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
        mlc: MythicLineConfig
    ) : CustomMechanic(cm, mlc) {

        private val source = mlc.getPlaceholderString(arrayOf("source", "s"), "")

        override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
            val attributeSource = source.get(meta)
            Atziluth.tempAttributeDataManager.takeAttribute(entity, attributeSource)
            return SkillResult.SUCCESS
        }

    }

}
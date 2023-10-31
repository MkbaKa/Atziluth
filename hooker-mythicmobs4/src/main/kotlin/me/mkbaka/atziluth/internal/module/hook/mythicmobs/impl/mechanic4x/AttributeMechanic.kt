package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeData
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool
import taboolib.common5.clong
import java.util.*

class AttributeMechanic {

    @MythicAnnotations.SkillMechanic(["add-attr", "addattr", "attr-add", "attradd"])
    class Add(mlc: MythicLineConfig): CustomMechanic(mlc) {

        private val source = mlc.getPlaceholderString(arrayOf("source", "s"), UUID.randomUUID().toString())
        private val timeout = mlc.getPlaceholderString(arrayOf("timeout", "time", "t"), "-1")
        private val merge = mlc.getPlaceholderString(arrayOf("merge", "m"), "false")

        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                val attributeSource = source.get(meta)
                val attrs = parseToAttribute(meta)

                Atziluth.tempAttributeDataManager.addAttribute(
                    entity, TempAttributeData.new(entity.uniqueId, attributeSource, attrs) {
                        this.timeout = this@Add.timeout.get(meta).clong
                    }, merge.get(meta).cbool
                )
            }
            return true
        }

    }

    @MythicAnnotations.SkillMechanic(["take-attr", "takeattr", "attr-take", "attrtake"])
    class Take(mlc: MythicLineConfig): CustomMechanic(mlc) {

        private val source = mlc.getPlaceholderString(arrayOf("source", "s"), "")

        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false
            val totalSource = source.get(meta)

            if (totalSource.isNotEmpty()) {
                Atziluth.tempAttributeDataManager.takeAttribute(entity, totalSource)
            }
            return true
        }

    }

}
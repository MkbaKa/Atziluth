package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.clong
import java.util.*

class AttributeMechanicIV {

    class Add(mlc: MythicLineConfig) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill {

        private val source = PlaceholderString.of(mlc.getString(arrayOf("source", "s"), UUID.randomUUID().toString()))
        private val timeout = PlaceholderString.of(mlc.getString(arrayOf("timeout", "t"), "0"))

        private val attrs =
            mlc.entrySet().filter { it.key.lowercase() !in AbstractMythicMobsHooker.ignoreSkillArgs }
                .flatMap { listOf("${it.key}: ${it.value}") }

        /**
         * add-attr{source=test;物理伤害=100} @Self
         * add-attr{source=test;timeout=60;物理伤害=100} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                AttributeBridge.addAttr(entity, source.get(meta), attrs, timeout.get(meta).clong)
            }
            return true
        }
    }

    class Take(mlc: MythicLineConfig) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill {

        private val source = PlaceholderString.of(mlc.getString(arrayOf("source", "s"), UUID.randomUUID().toString()))

        /**
         * take-attr{source=test} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                AttributeBridge.takeAttr(entity, source.get(meta))
            }
            return true
        }

    }
}
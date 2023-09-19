package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.mechanic

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString
import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillMechanicIV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.MythicMobsHookerImplIV
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.clong
import java.util.*

class AttributeMechanicIV {

    @CustomSkillMechanic(["add-attr", "addattr"], MythicMobVersion.IV)
    class Add(mlc: MythicLineConfig) : CustomSkillMechanicIV(mlc) {

        private val source = PlaceholderString.of(mlc.getString(arrayOf("source", "s"), UUID.randomUUID().toString()))
        private val timeout = PlaceholderString.of(mlc.getString(arrayOf("timeout", "time", "t"), "0"))

        private val entries = mlc.entrySet()

        /**
         * add-attr{source=test;物理伤害=100} @Self
         * add-attr{source=test;timeout=60;物理伤害=100} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                AttributeBridge.addAttr(
                    entity,
                    source.get(meta),
                    MythicMobsHookerImplIV.parse(entries, meta),
                    timeout.get(meta).clong
                )
            }
            return true
        }
    }

    @CustomSkillMechanic(["take-attr", "takeattr"], MythicMobVersion.IV)
    class Take(mlc: MythicLineConfig) : CustomSkillMechanicIV(mlc) {

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
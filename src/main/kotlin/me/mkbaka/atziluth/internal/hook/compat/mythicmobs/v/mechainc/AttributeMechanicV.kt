package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.mechainc

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import me.mkbaka.atziluth.internal.bridge.AttributeBridge
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.CustomSkillMechanicV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.MythicMobsHookerImplV
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity
import taboolib.common5.clong
import java.util.*

class AttributeMechanicV {

    @CustomSkillMechanic(["add-attr", "addattr"], MythicMobVersion.V)
    class Add(cm: CustomMechanic, mlc: MythicLineConfig) : CustomSkillMechanicV(cm, mlc) {

        private val source = PlaceholderString.of(mlc.getString(arrayOf("source", "s"), UUID.randomUUID().toString()))
        private val timeout = PlaceholderString.of(mlc.getString(arrayOf("timeout", "t"), "0"))

        private val entries = mlc.entrySet()

        /**
         * add-attr{source=test;物理伤害=100} @Self
         * add-attr{source=test;timeout=60;物理伤害=100} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
            val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

            if (entity.isAlive) {
                AttributeBridge.addAttr(
                    entity,
                    source.get(meta),
                    MythicMobsHookerImplV.parse(entries, meta),
                    timeout.get(meta).clong
                )
            }
            return SkillResult.SUCCESS
        }

    }

    @CustomSkillMechanic(["take-attr", "takeattr"], MythicMobVersion.V)
    class Take(cm: CustomMechanic, mlc: MythicLineConfig) : CustomSkillMechanicV(cm, mlc) {

        private val source = PlaceholderString.of(mlc.getString(arrayOf("source", "s"), UUID.randomUUID().toString()))

        /**
         * take-attr{source=test} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
            val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

            if (entity.isAlive) {
                AttributeBridge.takeAttr(entity, source.get(meta))
            }
            return SkillResult.SUCCESS
        }

    }
}
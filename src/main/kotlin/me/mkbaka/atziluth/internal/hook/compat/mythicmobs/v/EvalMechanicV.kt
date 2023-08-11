package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scriptreader.ScriptType
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

class EvalMechanicV {

    class JavaScript(cm: CustomMechanic, mlc: MythicLineConfig) : SkillMechanic(cm.manager, cm.file, mlc.line, mlc), ITargetedEntitySkill {

        private val script = mlc.getString(arrayOf("script", "s"), "")
        private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

        /**
         * eval-javascript{script=info("run! " + args1);args1=666} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
            val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

            if (entity.isAlive) {
                ScriptReader.create(ScriptType.JAVASCRIPT, """
                    function invoke() {
                        $script
                    }
                """.trimIndent()).invoke("invoke", args.apply { put("meta", meta) })
            }
            return SkillResult.SUCCESS
        }

    }

    class Kether(cm: CustomMechanic, mlc: MythicLineConfig) : SkillMechanic(cm.manager, cm.file, mlc.line, mlc), ITargetedEntitySkill {

        private val script = mlc.getString(arrayOf("script", "s"), "")
        private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

        /**
         * eval-kether{script=tell &args1;args1=114514} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
            val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

            if (entity.isAlive) {
                ScriptReader.create(ScriptType.KETHER, script).eval(entity, args.apply { put("meta", meta) })
            }
            return SkillResult.SUCCESS
        }

    }
}
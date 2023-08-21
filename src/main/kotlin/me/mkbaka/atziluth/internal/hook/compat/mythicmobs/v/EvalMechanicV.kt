package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scriptreader.ScriptType
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

class EvalMechanicV {

    @CustomSkillMechanic(["eval-javascript", "evaljavascript"], MythicMobVersion.V)
    class JavaScript(cm: CustomMechanic, mlc: MythicLineConfig) : CustomSkillMechanicV(cm, mlc) {

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

    @CustomSkillMechanic(["eval-kether", "evalkether"], MythicMobVersion.V)
    class Kether(cm: CustomMechanic, mlc: MythicLineConfig) : CustomSkillMechanicV(cm, mlc) {

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
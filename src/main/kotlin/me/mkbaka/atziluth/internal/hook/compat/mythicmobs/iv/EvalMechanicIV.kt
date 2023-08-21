package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scriptreader.ScriptType
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

class EvalMechanicIV {

    @CustomSkillMechanic(["eval-javascript", "evaljavascript"], MythicMobVersion.IV)
    class JavaScript(mlc: MythicLineConfig) : CustomSkillMechanicIV(mlc) {

        private val script = mlc.getString(arrayOf("script", "s"), "")
        private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

        /**
         * eval-javascript{script=info("run! " + args1);args1=666} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                ScriptReader.create(ScriptType.JAVASCRIPT, """
                    function invoke() {
                        $script
                    }
                """.trimIndent()).invoke("invoke", args.apply { put("meta", meta) })
            }
            return true
        }

    }

    @CustomSkillMechanic(["eval-kether", "evalkether"], MythicMobVersion.IV)
    class Kether(mlc: MythicLineConfig) : CustomSkillMechanicIV(mlc) {

        private val script = mlc.getString(arrayOf("script", "s"), "")
        private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

        /**
         * eval-kether{script=tell &args1;args1=114514} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                ScriptReader.create(ScriptType.KETHER, script).eval(entity, args.apply { put("meta", meta) })
            }
            return true
        }

    }
}
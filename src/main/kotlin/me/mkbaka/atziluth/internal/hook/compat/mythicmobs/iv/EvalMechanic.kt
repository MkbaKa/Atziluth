package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill
import io.lumine.xikage.mythicmobs.skills.SkillMechanic
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scriptreader.ScriptType
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

class EvalMechanic {

    class JavaScript(mlc: MythicLineConfig) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill {

        private val script = mlc.getPlaceholderString(arrayOf("script", "s"), "")
        private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

        /**
         * eval-javascript{script=info("run! " + args1);args1=666} @Self
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                ScriptReader.create(ScriptType.JAVASCRIPT, script.get(meta)).eval(entity, args)
            }
            return true
        }

    }

    class Kether(mlc: MythicLineConfig) : SkillMechanic(mlc.line, mlc), ITargetedEntitySkill {

        private val script = mlc.getPlaceholderString(arrayOf("script", "s"), "")
        private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

        /**
         * eval-kether{script=print "test &args1";args1=114514}
         */
        override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false

            if (entity.isAlive) {
                ScriptReader.create(ScriptType.KETHER, script.get(meta)).eval(entity, args)
            }
            return true
        }

    }
}
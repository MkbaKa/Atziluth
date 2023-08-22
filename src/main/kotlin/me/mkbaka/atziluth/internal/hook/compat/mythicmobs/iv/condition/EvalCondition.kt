package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.condition

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillCondition
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillConditionIV
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.scriptreader.ScriptType
import org.bukkit.entity.LivingEntity
import taboolib.common5.cbool

class EvalCondition {

    @CustomSkillCondition(["js-condition", "jscondition", "jsc"], MythicMobVersion.IV)
    class JavaScript(mlc: MythicLineConfig) : CustomSkillConditionIV(mlc) {

        private val script = mlc.getString(arrayOf("script", "s"), "")

        override fun check(ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false
            return ScriptReader.create(ScriptType.JAVASCRIPT, """
                    function invoke() {
                        $script
                    }
                """.trimIndent()).invoke("invoke", hashMapOf(
                "entity" to entity
            )).cbool
        }

    }

    @CustomSkillCondition(["kether-condition", "kethercondition", "ketherc"], MythicMobVersion.IV)
    class Kether(mlc: MythicLineConfig) : CustomSkillConditionIV(mlc) {

        private val script = mlc.getString(arrayOf("script", "s"), "")

        override fun check(ae: AbstractEntity): Boolean {
            val entity = ae.bukkitEntity as? LivingEntity ?: return false
            return ScriptReader.create(ScriptType.KETHER, script).eval(entity, emptyMap()).cbool
        }

    }
}
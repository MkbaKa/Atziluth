package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.mechainc

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import me.mkbaka.atziluth.internal.configuration.ScriptManager
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.CustomSkillMechanicV
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

@CustomSkillMechanic(["invoke-script", "invokescript"], MythicMobVersion.V)
class InvokeMechanicV(cm: CustomMechanic, mlc: MythicLineConfig) : CustomSkillMechanicV(cm, mlc) {

    private val path = mlc.getPlaceholderString(arrayOf("file", "path", "p"), "")
    private val func = mlc.getPlaceholderString(arrayOf("func", "function"), "")
    private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

    /**
     * invoke-script{file="example.js";func="onLoad";args1="asdasd"} @Self
     * invoke-script{file="test.yml";func="onLoad";args1="asdasd"} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): SkillResult {
        val entity = ae.bukkitEntity as? LivingEntity ?: return SkillResult.INVALID_TARGET

        if (entity.isAlive) {
            val func = func.get(meta)
            if (func.isEmpty()) return SkillResult.INVALID_CONFIG
            val script = ScriptManager.scripts[path.get(meta)] ?: return SkillResult.INVALID_CONFIG

            script.invoke(func, args)
        }
        return SkillResult.SUCCESS
    }

}
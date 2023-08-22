package me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.mechanic

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.configuration.ScriptManager
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.AbstractMythicMobsHooker
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.CustomSkillMechanic
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.MythicMobVersion
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillMechanicIV
import me.mkbaka.atziluth.internal.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

@CustomSkillMechanic(["invoke-script", "invokescript"], MythicMobVersion.IV)
class InvokeMechanicIV(mlc: MythicLineConfig) : CustomSkillMechanicIV(mlc) {

    private val file = mlc.getPlaceholderString(arrayOf("file", "path", "p"), "")
    private val func = mlc.getPlaceholderString(arrayOf("func", "function"), "")
    private val args = AbstractMythicMobsHooker.parseArgsMap(mlc.entrySet())

    /**
     * invoke-script{file="example.js";func="onLoad";args1="asdasd"} @Self
     * invoke-script{file="test.yml";func="onLoad";args1="asdasd"} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        if (entity.isAlive) {
            val func = func.get(meta)
            if (func.isEmpty()) return false
            val script = ScriptManager.scripts[file.get(meta)] ?: return false

            script.invoke(func, args)
        }
        return true
    }

}
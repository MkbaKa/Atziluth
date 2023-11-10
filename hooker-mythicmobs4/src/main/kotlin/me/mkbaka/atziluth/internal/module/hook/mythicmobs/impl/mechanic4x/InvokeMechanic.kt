package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechanic4x

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.configuration.impl.ScriptsComponent
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import me.mkbaka.atziluth.utils.EntityUtil.isAlive
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["invoke-script", "invokescript"])
class InvokeMechanic(mlc: MythicLineConfig) : CustomMechanic(mlc) {

    private val file = mlc.getPlaceholderString(arrayOf("file", "path", "p"), "")
    private val func = mlc.getPlaceholderString(arrayOf("func", "function"), "")

    /**
     * invoke-script{file="example.js";func="onLoad";args1="asdasd"} @Self
     * invoke-script{file="test.yml";func="onLoad";args1="asdasd"} @Self
     */
    override fun castAtEntity(meta: SkillMetadata, ae: AbstractEntity): Boolean {
        val entity = ae.bukkitEntity as? LivingEntity ?: return false

        if (entity.isAlive) {
            val func = func.get(meta)
            if (func.isEmpty()) return false
            val script = ScriptsComponent.scripts[file.get(meta)] ?: return false

            script.invokeFunction(func, parseAllEntries(meta))
        }
        return true
    }

}
package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.mechainc544

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import me.mkbaka.atziluth.internal.configuration.impl.ScriptsComponent
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import org.bukkit.entity.LivingEntity

@MythicAnnotations.SkillMechanic(["invoke-script", "invokescript"])
class InvokeMechanic(
    cm: io.lumine.mythic.core.skills.mechanics.CustomMechanic,
    mlc: MythicLineConfig
) : CustomMechanic(cm, mlc) {

    private val filePath = mlc.getPlaceholderString(arrayOf("file", "path", "p"), "")
    private val func = mlc.getPlaceholderString(arrayOf("func", "function"), "")

    /**
     * invoke-script{file="example.js";func="onLoad";args1="asdasd"} @Self
     * invoke-script{file="test.yml";func="onLoad";args1="asdasd"} @Self
     */
    override fun castToEntity(caster: LivingEntity, entity: LivingEntity, meta: SkillMetadata): SkillResult {
        val func = func.get(meta)
        if (func.isEmpty()) return SkillResult.INVALID_CONFIG
        val script = ScriptsComponent.scripts[filePath.get(meta)] ?: return SkillResult.INVALID_CONFIG

        script.invokeFunction(func, parseAllEntries(meta))
        return SkillResult.SUCCESS
    }

}
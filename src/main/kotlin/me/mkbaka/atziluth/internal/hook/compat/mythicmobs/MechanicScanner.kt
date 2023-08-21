package me.mkbaka.atziluth.internal.hook.compat.mythicmobs

import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillMechanicIV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.CustomSkillMechanicV
import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake

object MechanicScanner {

    private val packageName by lazy { this::class.java.`package`.name }

    @Awake(LifeCycle.ENABLE)
    fun scan() {

        runningClasses.forEach { clazz ->
            if (clazz.`package`.name.startsWith(packageName) && clazz.isAnnotationPresent(CustomSkillMechanic::class.java)) {
                val mechanic = clazz.getAnnotation(CustomSkillMechanic::class.java)

                when (mechanic.version) {
                    MythicMobVersion.IV -> mechanic.names.forEach { name -> CustomSkillMechanicIV.skills[name] = clazz as Class<CustomSkillMechanicIV> }
                    MythicMobVersion.V -> mechanic.names.forEach { name -> CustomSkillMechanicV.skills[name] = clazz as Class<CustomSkillMechanicV> }
                }
            }
        }

    }
}
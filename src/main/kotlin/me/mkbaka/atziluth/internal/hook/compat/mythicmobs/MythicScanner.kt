package me.mkbaka.atziluth.internal.hook.compat.mythicmobs

import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillConditionIV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.CustomSkillMechanicIV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.iv.MythicMobsHookerImplIV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.CustomSkillConditionV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.CustomSkillMechanicV
import me.mkbaka.atziluth.internal.hook.compat.mythicmobs.v.MythicMobsHookerImplV
import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake

object MythicScanner {

    private val packageName by lazy { this::class.java.`package`.name }

    private val mechanicAnnotation by lazy { CustomSkillMechanic::class.java }
    private val conditionAnnotation by lazy { CustomSkillCondition::class.java }

    @Awake(LifeCycle.ENABLE)
    fun scan() {

        runningClasses.forEach { clazz ->
            if (clazz.`package`.name.startsWith(packageName)) {
                registerSkillMechanic(clazz)
                registerSkillCondition(clazz)
            }
        }

    }

    fun registerSkillMechanic(clazz: Class<*>) {
        if (!clazz.isAnnotationPresent(mechanicAnnotation)) return
        val mechanic = clazz.getAnnotation(mechanicAnnotation)

        when (mechanic.version) {
            MythicMobVersion.IV -> mechanic.names.forEach { name -> MythicMobsHookerImplIV.skills[name] = clazz as Class<CustomSkillMechanicIV> }
            MythicMobVersion.V -> mechanic.names.forEach { name -> MythicMobsHookerImplV.skills[name] = clazz as Class<CustomSkillMechanicV> }
        }
    }

    fun registerSkillCondition(clazz: Class<*>) {
        if (!clazz.isAnnotationPresent(conditionAnnotation)) return
        val mechanic = clazz.getAnnotation(conditionAnnotation)

        when (mechanic.version) {
            MythicMobVersion.IV -> mechanic.names.forEach { name -> MythicMobsHookerImplIV.conditions[name] = clazz as Class<CustomSkillConditionIV> }
            MythicMobVersion.V -> mechanic.names.forEach { name -> MythicMobsHookerImplV.conditions[name] = clazz as Class<CustomSkillConditionV> }
        }
    }

}
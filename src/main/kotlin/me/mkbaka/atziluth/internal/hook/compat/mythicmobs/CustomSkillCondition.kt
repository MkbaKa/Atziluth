package me.mkbaka.atziluth.internal.hook.compat.mythicmobs

@Target(AnnotationTarget.CLASS)
annotation class CustomSkillCondition(
    val names: Array<String>,
    val version: MythicMobVersion
)
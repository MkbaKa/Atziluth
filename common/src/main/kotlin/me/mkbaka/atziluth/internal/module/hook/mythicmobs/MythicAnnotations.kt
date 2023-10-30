package me.mkbaka.atziluth.internal.module.hook.mythicmobs

class MythicAnnotations {

    @Target(AnnotationTarget.CLASS)
    annotation class SkillMechanic(val names: Array<String>)

    @Target(AnnotationTarget.CLASS)
    annotation class SkillCondition(val names: Array<String>)

}
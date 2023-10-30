package me.mkbaka.atziluth.utils.init

@Target(AnnotationTarget.FIELD)
annotation class InitBy(val path: String = "")

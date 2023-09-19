package me.mkbaka.atziluth.api.interfaces

import taboolib.common.io.runningClasses

abstract class Reloadable(val priority: Int) {

    abstract fun reload()

    companion object {

        private val reloadable = HashSet<Reloadable>()

        fun scan() {
            runningClasses.forEach { clazz ->
                if (Reloadable::class.java.isAssignableFrom(clazz) && clazz.simpleName != "Reloadable") {
                    reloadable.add(clazz.getField("INSTANCE").get(null) as Reloadable)
                }
            }
        }

        fun reloadAll() {
            reloadable.sortedBy { it.priority }.forEach(Reloadable::reload)
        }

    }

}
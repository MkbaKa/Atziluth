package me.mkbaka.atziluth.api.interfaces

abstract class Reloadable(val priority: Int) {

    abstract fun reload()

    init {
        register()
    }

    fun register() {
        reloadable.add(this)
    }

    companion object {

        private val reloadable = HashSet<Reloadable>()

        fun reloadAll() {
            reloadable.sortedBy { it.priority }.forEach(Reloadable::reload)
        }

    }

}
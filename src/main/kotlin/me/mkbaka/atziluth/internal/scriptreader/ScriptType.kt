package me.mkbaka.atziluth.internal.scriptreader

enum class ScriptType(vararg val names: String) {
    JAVASCRIPT("js"),
    KETHER("ke", "ks", "kether");

    companion object {

        fun of(string: String): ScriptType? {
            return values().firstOrNull { string.lowercase() in it.names }
        }

    }

}
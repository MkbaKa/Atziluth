package me.mkbaka.atziluth.utils

object Util {

    fun <T> List<T>.getOrDef(index: Int, def: T): T {
        return getOrNull(index) ?: def
    }

    fun DoubleArray.append(values: DoubleArray): DoubleArray {
        values.forEachIndexed { index, value ->
            this[index] = this.getOrDef(index, 0.0) + value
        }
        return this
    }

    fun DoubleArray.mapBy(func: (Double) -> Double): DoubleArray {
        this.forEachIndexed { index, value ->
            this[index] = func(value)
        }
        return this
    }

    fun DoubleArray.getOrDef(index: Int, def: Double): Double {
        return this.getOrElse(index) { def }
    }

}
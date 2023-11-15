package me.mkbaka.atziluth.utils

object AttributeUtil {

    fun DoubleArray.append(values: DoubleArray): DoubleArray {
        if (this.isNotEmpty()) {
            values.forEachIndexed { index, value ->
                this[index] = this.getOrDef(index, 0.0) + value
            }
        }
        return this
    }

    fun DoubleArray.mapBy(func: (Double) -> Double): DoubleArray {
        if (this.isNotEmpty()) {
            this.forEachIndexed { index, value ->
                this[index] = func(value)
            }
        }
        return this
    }

    fun DoubleArray.getOrDef(index: Int, def: Double): Double {
        return this.getOrElse(index) { def }
    }

}
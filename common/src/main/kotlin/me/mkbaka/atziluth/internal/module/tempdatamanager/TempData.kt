package me.mkbaka.atziluth.internal.module.tempdatamanager

/**
 * 代表一个临时数据
 */
interface TempData<T> {

    /**
     * 数据值
     */
    val value: T

}
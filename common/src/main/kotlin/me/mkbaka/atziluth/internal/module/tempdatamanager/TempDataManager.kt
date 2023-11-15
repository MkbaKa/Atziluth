package me.mkbaka.atziluth.internal.module.tempdatamanager

import java.util.*

/**
 * 临时数据管理
 */
interface TempDataManager {


    /**
     * 获取数据
     * @param [uuid] uuid
     * @return [TempData<*>?]
     */
    fun getData(uuid: UUID): TempData<*>?

}
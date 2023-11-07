package me.mkbaka.atziluth.internal.module.tempdatamanager

import java.util.*

interface TempDataManager {

    fun getData(uuid: UUID): TempData<*>?

}
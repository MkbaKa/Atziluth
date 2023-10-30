package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.utils.AttributeUtil
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import me.mkbaka.atziluth.utils.map.PriorityMap
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object AttributeManagerComponent : AbstractConfigComponent(10) {

    val attributes = ConcurrentHashMap<String, CustomAttribute>()
    val priorityMap = PriorityMap<CustomAttribute>()

    override val release: Boolean
        get() = Atziluth.isInitAttributeHooker()

    override val folder: File
        get() = newFolder(getDataFolder(), "attributes", create = false)

    override val defaultConfigs: List<String>
        get() = listOf(
            "example.js",
//            "example.yml"
        )

    override fun reload() {
        attributes.clear()
        priorityMap.clear()

        folder.executeSubFiles { file ->
            AttributeUtil.registerAttribute(file)
        }

    }


}
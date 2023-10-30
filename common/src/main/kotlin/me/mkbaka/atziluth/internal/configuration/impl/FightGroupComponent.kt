package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.internal.module.fightgroup.FightGroup
import me.mkbaka.atziluth.internal.module.fightgroup.impl.FightGroupImpl
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object FightGroupComponent : AbstractConfigComponent(6) {

    val fightGroups = ConcurrentHashMap<String, FightGroup>()

    override val release: Boolean
        get() = false

    override val folder: File
        get() = newFolder(getDataFolder(), "fightgroup", create = false)

    override fun reload() {
        fightGroups.clear()

        folder.executeSubFiles { file ->
            val config = Configuration.loadFromFile(file)

            config.getKeys(false).forEach { section ->
                fightGroups.computeIfAbsent(section) {
                    val contexts = hashMapOf<String, String>()
                    config.getConfigurationSection("$section.context")?.let { context ->
                        context.getKeys(false).forEach { key ->
                            contexts[key] = context.getString(key)!!
                        }
                    }
                    FightGroupImpl(config.getString("$section.scripts")!!, contexts)
                }
            }

        }

    }

}
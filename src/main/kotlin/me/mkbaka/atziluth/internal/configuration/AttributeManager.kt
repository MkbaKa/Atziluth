package me.mkbaka.atziluth.internal.configuration

import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeFactory
import me.mkbaka.atziluth.internal.register.AttributeType
import me.mkbaka.atziluth.internal.register.AttributeType.*
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.utils.FileUtil.executeSubFiles
import me.mkbaka.atziluth.internal.utils.FileUtil.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common5.cbool
import java.util.concurrent.ConcurrentHashMap

object AttributeManager : Reloadable(priority = 6) {

    val folder by lazy {
        newFolder(
            getDataFolder(),
            "attributes"
        ) { file ->
            releaseResourceFile("${file.name}/example.js")
            releaseResourceFile("${file.name}/example.yml")
        }
    }

    val subAttributes = ConcurrentHashMap<String, AbstractCustomAttribute<*>>()

    /**
     * 注册属性
     */
    override fun reload() {
        subAttributes.clear()
        AttributeFactory.onReload()

        folder.executeSubFiles { file ->
            if (file.extension != "js" && file.extension != "yml") return@executeSubFiles
            val reader = ScriptReader.create(file)

            AttributeFactory.buildAttribute(
                reader.getTopLevel("priority", -1),
                reader.getTopLevel("attributeName")!!,
                reader.getTopLevel("placeholder")!!,
                reader.getTopLevel("combatPower", 1.0),
                AttributeType.of(reader.getTopLevel("type", "other"))!!
            ).apply {
                if (reader.isFunction("onLoad")) onLoad = { attr ->
                    reader.invoke("onLoad", hashMapOf("args" to hashMapOf("Attr" to attr)))
                }

                if (this.type.function.isNotEmpty() && reader.isFunction(this.type.function)) {

                    when (this.type) {
                        ATTACK, DEFENSE -> callback = { attr, attacker, entity, args ->
                            reader.invoke(
                                this.type.function,
                                hashMapOf("args" to args.apply {
                                    this["Attr"] = attr
                                    this["attacker"] = attacker
                                    this["entity"] = entity
                                })
                            ).cbool
                        }

                        RUNTIME -> {
                            runtimeCallback = { attr, player ->
                                reader.invoke(
                                    this.type.function,
                                    hashMapOf("args" to hashMapOf(
                                        "Attr" to attr, "player" to player, "entity" to player
                                    ))
                                ).cbool
                            }
                            this.period = reader.getTopLevel("period", 5L)
                        }

                        OTHER -> Unit
                    }

                }

                subAttributes[this.name] = this
            }
        }

        AttributeFactory.registerAttributes(subAttributes.values.sortedBy { attr -> attr.attrPriority })
    }

}
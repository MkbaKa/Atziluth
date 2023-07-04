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
            releaseResourceFile("${file.name}/example.ks")
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
            if (file.extension != "js" && file.extension != "ks") return@executeSubFiles
            val reader = ScriptReader.create(file)

            AttributeFactory.buildAttribute(
                reader.getTopLevel("priority"),
                reader.getTopLevel("attributeName"),
                reader.getTopLevel("placeholder"),
                reader.getTopLevel("combatPower"),
                AttributeType.of(reader.getTopLevel("type"))!!
            ).apply {
                if (reader.isFunction("onLoad")) onLoad { attr ->
                    reader.invoke("onLoad", hashMapOf("Attr" to attr))
                }

                if (this.type.function.isNotEmpty() && reader.isFunction(this.type.function)) {
                    when (this.type) {
                        ATTACK, DEFENSE -> callback { attr, attacker, entity ->
                            reader.invoke(
                                this.type.function,
                                hashMapOf("Attr" to attr, "attacker" to attacker, "entity" to entity)
                            ).cbool
                        }

                        RUNTIME -> run { attr, player ->
                            reader.invoke(
                                this.type.function,
                                hashMapOf("Attr" to attr, "player" to player, "entity" to player)
                            ).cbool
                        }

                        OTHER -> Unit
                    }
                }

                if (this.type == RUNTIME) this.period = reader.getTopLevel<Long>("period") ?: 5L

                subAttributes[this.name] = this
            }
        }

        AttributeFactory.registerAttributes(subAttributes.values.sortedBy { attr -> attr.attrPriority })
    }

}
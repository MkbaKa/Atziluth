package me.mkbaka.atziluth.internal.configuration

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.api.interfaces.Reloadable
import me.mkbaka.atziluth.internal.register.AbstractCustomAttribute
import me.mkbaka.atziluth.internal.register.AttributeFactory
import me.mkbaka.atziluth.internal.register.AttributeType
import me.mkbaka.atziluth.internal.scriptreader.ScriptReader
import me.mkbaka.atziluth.internal.utils.FileUtil.executeSubFiles
import taboolib.common.LifeCycle
import taboolib.common.io.newFile
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.cbool
import java.util.concurrent.ConcurrentHashMap

object AttributeManager : Reloadable(priority = 6) {

    val folder by lazy { newFile(getDataFolder(), "attributes", create = true, folder = true) }

    val subAttributes = ConcurrentHashMap<String, AbstractCustomAttribute<*, *>>()

    @Awake(LifeCycle.ACTIVE)
    fun enable() {
//        releaseResourceFile("${folder.name}/example.js", replace = false)
//        releaseResourceFile("${folder.name}/example.ks", replace = false)
    }

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
                AttributeType.of(reader.getTopLevel("type"))
            ).apply {
                if (reader.isFunction("onLoad")) onLoad {
                    reader.invoke("onLoad", hashMapOf("Attr" to it!!))
                }
                if (this.type != AttributeType.OTHER && reader.isFunction(this.type.function)) {
                    callback { attr, attacker, entity ->
                        reader.invoke(
                            this.type.function,
                            hashMapOf("attacker" to attacker, "entity" to entity, "Attr" to attr!!)
                        ).cbool
                    }
                }

                subAttributes[name] = this
            }
        }

        subAttributes.values.sortedBy { it.attrPriority }.forEach { attrImpl -> attrImpl.register() }
        Atziluth.attributeFactory.registeredCallback()
    }

}
package me.mkbaka.atziluth.internal.configuration.impl

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.configuration.AbstractConfigComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute.Companion.buildAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.utils.FileUtil.executeSubFiles
import me.mkbaka.atziluth.utils.enumOf
import me.mkbaka.atziluth.utils.map.PriorityMap
import org.bukkit.Bukkit
import taboolib.common.io.newFolder
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
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
            try {
                registerAttribute(buildAttribute(file))
            } catch (e: Throwable) {
                Bukkit.getConsoleSender().sendMessage("§4脚本加载时出现错误! §f${file.path}: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }

        priorityMap.forEach { (priority, attr) ->
            console().sendLang(
                "register-attribute",
                Atziluth.prefix,
                attr.attributeName,
                priority,
                attr.combatPower,
                attr.placeholder,
                attr.attributeType.name
            )
        }
    }

    fun registerAttribute(attribute: CustomAttribute) {
        if (!release) error("属性模块未启用, 无法注册属性.")
        if (attributes.containsKey(attribute.attributeName)) error("${attribute.attributeName} 已被注册, 请尝试修改为其他名称.")
        attributes[attribute.attributeName] = attribute.apply {
            if (attributeType != CustomAttributeType.OTHER) priorityMap[this.priority] = this
            Atziluth.attributeHooker.registerOtherAttribute(attributeName, combatPower, placeholder)
            onLoad()
        }
    }

    fun registerOtherAttribute(attributeName: String, combatPower: Double, placeholder: String) {
        registerAttribute(buildAttribute(attributeName, CustomAttributeType.OTHER, placeholder) {
            this.combatPower = combatPower
        })
    }

    fun buildAttribute(file: File): CustomAttribute {

        val script = AbstractScriptFactory.compile(file)
            ?: error("无法根据 ${file.path} 的后缀 ${file.extension} 获取脚本处理器")

        val attributeName =
            script.getTopLevel<String>("attributeName") ?: error("${file.path} | 无法获取到顶级变量 attributeName")
        val type = script.getTopLevel<String>("type") ?: error("${file.path} | 无法获取到顶级变量 type")
        val attributeType = enumOf<CustomAttributeType>(type) ?: error("${file.path} | 未找到名为 $type 的属性类型")
        val placeholder =
            script.getTopLevel<String>("placeholder") ?: error("${file.path} | 无法获取到顶级变量 placeholder")

        return buildAttribute(attributeName, attributeType, placeholder) {
            this.isBefore = script.getTopLevel("isBefore", false)
            this.priority = script.getTopLevel("priority", -1)
            this.combatPower = script.getTopLevel("combatPower", 1.0)
            this.period = script.getTopLevel("period", 5)

            if (script.isFunction("onLoad")) this.onLoad = { ->
                script.invokeFunction("onLoad", hashMapOf("Attr" to this))
            }

            if (script.isFunction(this.attributeType.function)) {
                when (this.attributeType) {
                    CustomAttributeType.ATTACK, CustomAttributeType.DEFENSE -> this.callback =
                        { fightData, args ->
                            script.invokeFunction(
                                this.attributeType.function,
                                hashMapOf(
                                    "attacker" to fightData.attacker,
                                    "entity" to fightData.entity,
                                    "fightData" to fightData,
                                    "Attr" to this
                                ).apply { putAll(args) })
                        }

                    CustomAttributeType.RUNTIME, CustomAttributeType.UPDATE -> this.run = { entity ->
                        script.invokeFunction(
                            this.attributeType.function,
                            hashMapOf("entity" to entity, "Attr" to this)
                        )
                    }

                    CustomAttributeType.OTHER -> {}
                }
            }

        }
    }

}
package me.mkbaka.atziluth.utils

import me.mkbaka.atziluth.Atziluth
import me.mkbaka.atziluth.internal.configuration.impl.AttributeManagerComponent
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttribute
import me.mkbaka.atziluth.internal.module.attributes.attribute.CustomAttributeType
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object AttributeUtil {

    fun DoubleArray.append(values: DoubleArray): DoubleArray {
        if (this.isNotEmpty()) {
            values.forEachIndexed { index, value ->
                this[index] = (this.getOrNull(index) ?: 0.0) + value
            }
        }
        return this
    }

    fun registerAttribute(file: File): CustomAttribute {
        if (!AttributeManagerComponent.release) error("属性模块未启用, 无法注册属性.")

        val script = AbstractScriptFactory.compile(file)
            ?: error("无法根据 ${file.path} 的后缀 ${file.extension} 获取脚本处理器")

        val attributeName =
            script.getTopLevel<String>("attributeName") ?: error("${file.path} | 无法获取到顶级变量 attributeName")
        val type = script.getTopLevel<String>("type") ?: error("${file.path} | 无法获取到顶级变量 type")
        val attributeType = CustomAttributeType.of(type) ?: error("${file.path} | 未找到名为 $type 的属性类型")
        val placeholder =
            script.getTopLevel<String>("placeholder") ?: error("${file.path} | 无法获取到顶级变量 placeholder")

        return AttributeManagerComponent.attributes.computeIfAbsent(attributeName) {
            CustomAttribute.buildAttribute(attributeName, attributeType, placeholder) {
                this.isBefore = script.getTopLevel("isBefore", false)
                this.priority = script.getTopLevel("priority", -1)
                this.combatPower = script.getTopLevel("combatPower", 1.0)
                this.period = script.getTopLevel("period", 5)

                if (script.isFunction("onLoad")) this.onLoad = { attr ->
                    script.invokeFunction("onLoad", hashMapOf("Attr" to attr))
                }

                if (script.isFunction(this.attributeType.function)) {
                    when (this.attributeType) {
                        CustomAttributeType.ATTACK, CustomAttributeType.DEFENSE -> this.callback =
                            { attacker, entity, attr, args ->
                                script.invokeFunction(
                                    this.attributeType.function,
                                    hashMapOf(
                                        "attacker" to attacker,
                                        "entity" to entity,
                                        "Attr" to attr
                                    ).apply { putAll(args) })
                            }

                        CustomAttributeType.RUNTIME, CustomAttributeType.UPDATE -> this.run = { entity, attr ->
                            script.invokeFunction(
                                this.attributeType.function,
                                hashMapOf("entity" to entity, "Attr" to attr)
                            )
                        }

                        CustomAttributeType.OTHER -> {}
                    }
                }

                AttributeManagerComponent.priorityMap[this.priority] = this
            }
        }.apply {
            Atziluth.attributeHooker.registerOtherAttribute(this.attributeName, this.combatPower, this.placeholder)
            console().sendLang(
                "register-attribute",
                Atziluth.prefix,
                this.attributeName,
                this.priority,
                this.combatPower,
                this.placeholder
            )

            this.onLoad(this)
        }
    }

}
package me.mkbaka.atziluth

import me.mkbaka.atziluth.api.AttributeAPI
import me.mkbaka.atziluth.internal.configuration.impl.ScriptsComponent
import me.mkbaka.atziluth.internal.module.attributes.AttributePluginHooker
import me.mkbaka.atziluth.internal.module.attributes.AttributePlugins
import me.mkbaka.atziluth.internal.module.hook.HookerManager
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeDataManager
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempDataManager
import me.mkbaka.atziluth.utils.ClassUtil.instance
import me.mkbaka.atziluth.utils.init.InitBy
import me.mkbaka.atziluth.utils.init.Initializes
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.BukkitPlugin
import java.util.regex.Pattern

object Atziluth : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    val showName by lazy {
        // 高于 1.16 使用 rgb 颜色
        if (MinecraftVersion.isHigherOrEqual(8)) {
            "&{#5846E2}A&{#5C4BE3}t&{#6050E4}z&{#6455E5}i&{#685AE6}l&{#6C5FE7}u&{#7064E8}t&{#7469E9}h"
        } else {
            "&9Atz&3ilu&bth"
        }.colored()
    }

    val prefix by lazy { "&8[$showName&8]".colored() }

    val number_pattern by lazy { Pattern.compile("\\b(\\d+.\\d+)\\b") }

    val namespaces by lazy { listOf("Atziluth") }

    @InitBy("me.mkbaka.atziluth.api.AttributeAPI")
    lateinit var attributeAPI: AttributeAPI

    @InitBy("me.mkbaka.atziluth.internal.module.script.javascript.impl.factory.JavaScriptFactoryImpl")
    lateinit var javaScriptFactory: AbstractScriptFactory

    @InitBy("me.mkbaka.atziluth.internal.module.hook.HookerManager")
    lateinit var hookerManager: HookerManager

    @InitBy
    lateinit var tempDataManager: TempDataManager

    @InitBy
    lateinit var tempAttributeDataManager: TempAttributeDataManager

    lateinit var attributeHooker: AttributePluginHooker<*, *>

    override fun onEnable() {
        console().sendLang("plugin-enable", prefix, Bukkit.getBukkitVersion())

        // 获取可支持的属性插件
        AttributePlugins.find()?.let { plugin ->
            attributeHooker =
                Class.forName("me.mkbaka.atziluth.internal.module.attributes.impl.${plugin.hookerClass}").instance as AttributePluginHooker<*, *>
            val bukkitPlugin = Bukkit.getPluginManager().getPlugin(plugin.pluginName)!!
            console().sendLang("find-plugin", prefix, plugin.pluginName, bukkitPlugin.description.version)
        }

        // 初始化 lateinit 字段
        Initializes.initFields(this::class.java)

        // 初始化其他插件兼容
        HookerManager.init()

        // 调用脚本内的onEnable方法
        // @Awake(LifeCycle.ENALBE) 会在插件onEnable之前执行
        // 这时脚本内访问的诸多变量均未初始化 所以手动触发onEnable
        ScriptsComponent.enable()
    }

    fun isInitAttributeHooker() = kotlin.runCatching { this::attributeHooker.isInitialized }.getOrNull() ?: false

    // 给js用
    fun getInitializedAttributeHooker(): AttributePluginHooker<*, *>? {
        return if (isInitAttributeHooker()) attributeHooker else null
    }
}
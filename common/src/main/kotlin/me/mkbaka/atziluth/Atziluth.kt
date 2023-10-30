package me.mkbaka.atziluth

import me.mkbaka.atziluth.internal.module.attributes.AttributePluginHooker
import me.mkbaka.atziluth.internal.module.attributes.AttributePlugins
import me.mkbaka.atziluth.internal.module.hook.HookerManager
import me.mkbaka.atziluth.internal.module.script.AbstractScriptFactory
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempAttributeDataManager
import me.mkbaka.atziluth.internal.module.tempdatamanager.TempDataManager
import me.mkbaka.atziluth.utils.init.InitBy
import me.mkbaka.atziluth.utils.init.Initializes
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

object Atziluth : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    val prefix by lazy { "&8[&{#5846E2}A&{#5C4BE3}t&{#6050E4}z&{#6455E5}i&{#685AE6}l&{#6C5FE7}u&{#7064E8}t&{#7469E9}h&8]".colored() }

    val namespaces by lazy { listOf("Atziluth") }

    @InitBy("me.mkbaka.atziluth.internal.module.script.javascript.impl.factory.JavaScriptFactoryImpl")
    lateinit var javaScriptFactory: AbstractScriptFactory

    @InitBy
    lateinit var tempDataManager: TempDataManager

    @InitBy
    lateinit var tempAttributeDataManager: TempAttributeDataManager

    lateinit var attributeHooker: AttributePluginHooker<*, *>

    override fun onEnable() {
        console().sendLang("plugin-enable", prefix, Bukkit.getBukkitVersion())

        AttributePlugins.find()?.let { plugin ->
            attributeHooker = plugin.hooker.newInstance()
            val bukkitPlugin = Bukkit.getPluginManager().getPlugin(plugin.pluginName)!!
            console().sendLang("find-plugin", prefix, plugin.pluginName, bukkitPlugin.description.version)
        }

        HookerManager.init()

        Initializes.initClassFields(this::class.java)
    }

    // 无法理解为什么不让在其它类中直接获取 isInitialized
    fun isInitAttributeHooker() = this::attributeHooker.isInitialized
}
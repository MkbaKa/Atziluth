//package me.mkbaka.atziluth.internal.register.impl.sxattribute
//
//import github.saukiya.sxattribute.SXAttribute
//import github.saukiya.sxattribute.util.Config
//import me.mkbaka.atziluth.Atziluth
//import me.mkbaka.atziluth.Atziluth.prefix
//import me.mkbaka.atziluth.internal.register.AttributeFactory
//import me.mkbaka.atziluth.internal.register.AttributeType
//import org.bukkit.Bukkit
//import taboolib.common.platform.function.console
//import taboolib.library.reflex.Reflex.Companion.setProperty
//import taboolib.module.lang.sendLang
//
//object LegacySXAttributeImpl : AttributeFactory<LegacySXAttributeAdapter>() {
//
//    override fun registerAttribute(customAttribute: LegacySXAttributeAdapter) {
//        hacker(customAttribute) {
//            it.build().registerAttribute(Atziluth.plugin)
//        }
//    }
//
//    override fun reload() {
//        console().sendLang("unsupported-reload", prefix, Atziluth.attributePlugin.pluginName)
//    }
//
//    override fun buildAttribute(
//        priority: Int,
//        name: String,
//        placeholder: String,
//        combatPower: Double,
//        type: AttributeType
//    ): LegacySXAttributeAdapter {
//        return LegacySXAttributeAdapter(priority, name, placeholder, type)
//    }
//
//    private val mainClass by lazy { Bukkit.getPluginManager().getPlugin("SX-Attribute") as SXAttribute }
//
//    /**
//     * 太弱智了这个设计
//     * 一旦本体加载就禁止注册属性，条件，指令
//     */
//    private fun hacker(customAttribute: LegacySXAttributeAdapter, register: (LegacySXAttributeAdapter) -> Unit) {
//        saveToConfig(customAttribute)
//        mainClass.setProperty("pluginEnabled", false)
//        register(customAttribute)
//        mainClass.setProperty("pluginEnabled", true)
//    }
//
//    private fun saveToConfig(customAttribute: LegacySXAttributeAdapter) {
//        Config.getConfig().set("AttributePriority.${customAttribute.name}", customAttribute.priority)
//    }
//}
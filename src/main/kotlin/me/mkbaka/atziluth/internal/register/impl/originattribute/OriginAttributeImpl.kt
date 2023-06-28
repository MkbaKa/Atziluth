//package me.mkbaka.atziluth.internal.register.impl.originattribute
//
//import ac.github.oa.internal.core.attribute.AttributeManager
//import me.mkbaka.atziluth.internal.register.AttributeFactory
//import me.mkbaka.atziluth.internal.register.AttributeType
//import taboolib.common.platform.function.info
//
//object OriginAttributeImpl : AttributeFactory<OriginAttributeAdapter>() {
//
//    override fun reload() {
//        val iterator = AttributeManager.attributeInstances.iterator()
//        while (iterator.hasNext()) {
//            val entry = iterator.next()
//            if (entry.javaClass.`package`.name != packageName) continue
//            iterator.remove()
//        }
//    }
//
//    override fun buildAttribute(
//        priority: Int,
//        name: String,
//        placeholder: String,
//        combatPower: Double,
//        type: AttributeType
//    ): OriginAttributeAdapter {
//        return OriginAttributeAdapter(priority, name, placeholder, type)
//    }
//
//    override fun registerAttribute(customAttribute: OriginAttributeAdapter) {
//        customAttribute.build().let { attr ->
//            AttributeManager.registerAttribute(attr)
//            info("${attr.index} | ${attr.getPriority()}")
//            AttributeManager.attributeRegistry.apply {
//                add(attr.index.coerceAtMost(this.size), attr.toName())
//            }
//            AttributeManager.loadAttribute(attr)
//        }
//        info(AttributeManager.attributeInstances.map { it.toName() })
//        info(AttributeManager.usableAttributes.values.map { it.toName() })
//    }
//
//}
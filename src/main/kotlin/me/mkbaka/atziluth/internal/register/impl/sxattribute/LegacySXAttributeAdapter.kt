//package me.mkbaka.atziluth.internal.register.impl.sxattribute
//
//import github.saukiya.sxattribute.data.attribute.SXAttributeType
//import github.saukiya.sxattribute.data.attribute.SubAttribute
//import github.saukiya.sxattribute.data.eventdata.EventData
//import github.saukiya.sxattribute.data.eventdata.sub.DamageEventData
//import me.mkbaka.atziluth.internal.register.AttributeType
//import org.bukkit.entity.LivingEntity
//import org.bukkit.entity.Player
//import taboolib.common.util.random
//
//class LegacySXAttributeAdapter(
//    override val priority: Int,
//    override val name: String,
//    override val placeholder: String,
//    override val type: AttributeType
//) : CustomAttribute<SubAttribute, SXAttributeType>() {
//
//    override fun build(): SubAttribute {
//        return LegacySXAttribute(name, priority, getType(), callback)
//    }
//
//    override fun register() {
//        LegacySXAttributeImpl.registerAttribute(this)
//    }
//
//    override fun getType(): SXAttributeType {
//        return when (type) {
//            AttributeType.ATTACK -> SXAttributeType.DAMAGE
//            AttributeType.DEFENSE -> SXAttributeType.DEFENCE
//            AttributeType.RUNTIME, AttributeType.OTHER -> SXAttributeType.OTHER
//        }
//    }
//
//}
//
//class LegacySXAttribute : SubAttribute {
//
//    val callback: (LivingEntity, LivingEntity) -> Boolean
//
//    constructor(
//        name: String,
//        priority: Int,
//        type: SXAttributeType,
//        callback: (LivingEntity, LivingEntity) -> Boolean
//    ) : super(name, priority, type) {
//        this.callback = callback
//    }
//
//    override fun eventMethod(data: EventData) {
//        if (data !is DamageEventData) return
//        when (type) {
//            SXAttributeType.DAMAGE -> callback(data.damager, data.entity)
//            SXAttributeType.DEFENCE -> callback(data.entity, data.damager)
//            else -> error("Unsupported type $type")
//        }
//    }
//
//    override fun getPlaceholder(player: Player, str: String): String {
//        return if (str.equals(name, ignoreCase = true)) "${attributes[0]} - ${attributes[1]}" else "NaN"
//    }
//
//    override fun getPlaceholders(): MutableList<String> {
//        return mutableListOf(name)
//    }
//
//    override fun loadAttribute(str: String?): Boolean {
//        return true
//    }
//
//    override fun getAttrValue(): Double {
//        return random(attributes[0], attributes[1])
//    }
//
//}
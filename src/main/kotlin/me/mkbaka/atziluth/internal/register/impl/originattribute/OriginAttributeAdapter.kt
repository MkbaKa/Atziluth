//package me.mkbaka.atziluth.internal.register.impl.originattribute
//
//import ac.github.oa.internal.base.event.EventMemory
//import ac.github.oa.internal.base.event.impl.DamageMemory
//import ac.github.oa.internal.core.attribute.AbstractAttribute
//import ac.github.oa.internal.core.attribute.Attribute
//import ac.github.oa.internal.core.attribute.AttributeData
//import me.mkbaka.atziluth.internal.register.AttributeType
//import org.bukkit.entity.Player
//
//class OriginAttributeAdapter(
//    override val attrPriority: Int,
//    override val name: String,
//    override val placeholder: String,
//    override val type: AttributeType
//) : AbstractAttribute<AbstractAttribute, ac.github.oa.internal.core.attribute.AttributeType>() {
//
//    override fun build(): AbstractAttribute {
//        return object : AbstractAttribute() {
//
//            override val types: Array<ac.github.oa.internal.core.attribute.AttributeType>
//                get() = arrayOf(getType())
//
//            override var index: Int = super.index
//                get() = attrPriority
//                set(value) {
//                    field = value
//                }
//
////            override var root: ac.github.oa.ac.github.oa.taboolib.library.configuration.ConfigurationSection
////                get() =
////                set(value) {}
//
//            override fun onLoad() {
//                entries += entry
//                entry.name = name
//                entry.node = this
//                entry.onEnable()
//                onLoad?.let { it(this) }
//            }
//
//            override fun toName(): String {
//                return name
//            }
//
//            override fun toValue(player: Player, attributeData: AttributeData, args: Array<String>): Any? {
//                val arg = if (args.size == 3) args[2] else ""
//                return entry.toValue(player, arg, attributeData.getData(this.index, entry.index))
//            }
//
//            val entry = Entry(type, this)
//
//            inner class Entry(val attrType: AttributeType, val abstractAttribute: AbstractAttribute) : Attribute.Entry() {
//
//                override val type: Attribute.Type
//                    get() = Attribute.Type.RANGE
//
//                override fun handler(memory: EventMemory, data: AttributeData.Data) {
//                    if (memory !is DamageMemory) return
//                    when (attrType) {
//                        AttributeType.ATTACK -> callback?.let { it(abstractAttribute, memory.attacker, memory.injured) }
//                        AttributeType.DEFENSE -> callback?.let { it(abstractAttribute, memory.injured, memory.attacker) }
//                        else -> error("Unsupported attribute type ${attrType.name}")
//                    }
//                }
//
//            }
//
//        }
//    }
//
//    override fun register() {
//        OriginAttributeImpl.registerAttribute(this)
//    }
//
//    override fun getType(): ac.github.oa.internal.core.attribute.AttributeType {
//        return when (type) {
//            AttributeType.ATTACK -> ac.github.oa.internal.core.attribute.AttributeType.ATTACK
//            AttributeType.DEFENSE -> ac.github.oa.internal.core.attribute.AttributeType.DEFENSE
//            AttributeType.RUNTIME, AttributeType.OTHER -> ac.github.oa.internal.core.attribute.AttributeType.OTHER
//        }
//    }
//}
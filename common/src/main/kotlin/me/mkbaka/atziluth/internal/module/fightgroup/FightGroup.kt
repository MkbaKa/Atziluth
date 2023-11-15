package me.mkbaka.atziluth.internal.module.fightgroup

import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * 类似 AttributeSystem 的战斗组
 * 没想好怎么设计 先放着吧
 */
interface FightGroup {

    val scripts: String

    val contexts: Map<String, String>

    fun evalContext(evt: EntityDamageByEntityEvent): HashMap<String, Any>

    fun handle(context: Map<String, Any> = emptyMap()): Double

}
package me.mkbaka.atziluth.internal.module.fightgroup

import org.bukkit.event.entity.EntityDamageByEntityEvent

interface FightGroup {

    val scripts: String

    val contexts: Map<String, String>

    fun evalContext(evt: EntityDamageByEntityEvent): HashMap<String, Any>

    fun handle(context: Map<String, Any> = emptyMap()): Double

}
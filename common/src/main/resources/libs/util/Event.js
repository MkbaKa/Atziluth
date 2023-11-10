const EventUtil = Packages.me.mkbaka.atziluth.utils.EventUtil.INSTANCE

/**
 * 获取造成伤害时的蓄力进度
 * @param event EntityDamageByEntityEvent
 * @returns 0 ~ 1
 */
const getEventForce = function (event) {
    return EventUtil.getAttackCooldown(event)
}

const callEvent = function (event) {
    EventUtil.call(event)
}
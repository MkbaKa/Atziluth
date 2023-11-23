const VanillaDamageMeta = Packages.me.mkbaka.atziluth.internal.module.damage.impl.VanillaDamageMeta
const VanillaDamageOptions = Packages.me.mkbaka.atziluth.internal.module.damage.impl.VanillaDamageOptions
const AtziluthDamageMeta = Packages.me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageMeta
const AtziluthDamageOptions = Packages.me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions

/**
 * 造成有源原版伤害
 * 会触发所有属性
 * @param attacker 攻击方
 * @param entities 受击的实体列表
 * @param options me.mkbaka.atziluth.internal.module.damage.impl.VanillaDamageOptions
 */
const doDamages = function (attacker, entities, options) {
    new VanillaDamageMeta(attacker, entities, options).doDamage()
}

/**
 * 造成有源原版伤害
 * 会触发所有属性
 * @param attacker 攻击方
 * @param entity 受击方
 * @param options me.mkbaka.atziluth.internal.module.damage.impl.VanillaDamageOptions
 */
const doDamage = function (attacker, entity, options) {
    new VanillaDamageMeta(attacker, listOf(entity), options).doDamage()
}

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entities 受击的实体列表
 * @param options me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
 */
const doAttrDamages = function (attacker, entities, options) {
    new AtziluthDamageMeta(attacker, entities, options).doDamage()
}

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entity 受击方
 * @param options me.mkbaka.atziluth.internal.module.damage.impl.AtziluthDamageOptions
 */
const doAttrDamage = function (attacker, entity, options) {
    new AtziluthDamageMeta(attacker, listOf(entity), options).doDamage()
}

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entity 受击方
 * @param damageValue 伤害值
 */
const doClearAttrDamage = function (attacker, entity, damageValue) {
    new AtziluthDamageMeta(attacker, listOf(entity), AtziluthDamageOptions.new(function (builder) {
        builder.isClear = true
        builder.setDamageValue(damageValue)
    })).doDamage()
}

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entity 受击方
 * @param attrs 属性Map 格式为 Map<String, Array<Double>>
 * @param isClear 是否清除玩家身上的属性
 */
const doAttDamage = function (attacker, entity, attrs, isClear) {
    new AtziluthDamageMeta(attacker, listOf(entity), AtziluthDamageOptions.new(function (builder) {
        builder.setAttributes(attrs)
        builder.isClear = isClear
    })).doDamage()
}

/**
 * 造成属性伤害
 * @param attacker 攻击方
 * @param entity 受击方
 * @param attrs 属性Map 格式为 Map<String, Array<Double>>
 * @param whiteListAttrs 不会被清除的属性名列表
 * @param isClear 是否清除玩家身上的属性
 */
const doAttributeDamage = function (attacker, entity, attrs, whiteListAttrs, isClear) {
    new AtziluthDamageMeta(attacker, listOf(entity), AtziluthDamageOptions.new(function (builder) {
        builder.setAttributes(attrs)
        builder.setWhitelistAttributeNames(whiteListAttrs)
        builder.isClear = isClear
    })).doDamage()
}


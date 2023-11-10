const ItemUtil = Packages.me.mkbaka.atziluth.utils.ItemUtil.INSTANCE

/**
 * 懒得再写一遍注释了
 * @package me.mkbaka.atziluth.internal.utils.ItemUtil
 */

const isAir = function (item) {
    return ItemUtil.isAir(item)
}

const isType = function (item, target) {
    return ItemUtil.isType(item, target)
}

const takeItem = function (target, item, amount) {
    return ItemUtil.takeItem(target, item, amount)
}

const giveItem = function (item, target) {
    return ItemUtil.giveItem(target, item)
}

const hasName = function (item) {
    return ItemUtil.hasName(item, null)
}

const hasTargetName = function (item, name) {
    return ItemUtil.hasName(item, name)
}

const hasLore = function (item) {
    return ItemUtil.hasLore(item, null)
}

const hasTargetLore = function (item, lore) {
    return ItemUtil.hasLore(item, lore)
}

const getName = function (item) {
    return ItemUtil.getName(item)
}

const getItemName = function (item, target) {
    return ItemUtil.getName(item, target)
}

const hasNBT = function (item, nbtKey) {
    return ItemUtil.hasNBT(item, nbtKey)
}
package me.mkbaka.atziluth.utils

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.getName
import taboolib.platform.util.*

object ItemUtil {

    /**
     * 是否为空
     * @param [item] 物品
     * @return [Boolean]
     */
    fun isAir(item: ItemStack): Boolean {
        return item.isAir
    }

    /**
     * 判断物品材质
     * @param [item] 物品
     * @param [target] 材质
     * @return [Boolean]
     */
    fun isType(item: ItemStack, target: Material): Boolean {
        return item.type == target
    }

    /**
     * 判断物品材质
     * @param [item] 物品
     * @param [target] 材质
     * @return [Boolean]
     */
    fun isType(item: ItemStack, target: String): Boolean {
        return XMaterial.matchXMaterial(target).get().parseMaterial()?.equals(item.type) ?: false
    }

    /**
     * 扣除物品
     * @param [target] 目标
     * @param [item] 物品
     * @param [amount] 数量
     */
    fun takeItem(target: Player, item: ItemStack, amount: Int = 1) {
        target.inventory.takeItem(amount) { it == item }
    }

    /**
     * 给予物品
     * @param [target] 目标
     * @param [item] 物品
     */
    fun giveItem(target: Player, item: ItemStack) {
        target.giveItem(item)
    }

    /**
     * 物品名内是否包含指定内容
     * 若指定内容为空则判断物品是否拥有名字
     * @param [item] 物品
     * @param [target] 内容
     * @return [Boolean]
     */
    fun hasName(item: ItemStack, target: String? = null): Boolean {
        return item.hasName(target)
    }

    /**
     * 物品是否包含指定lore
     * 若指定描述为空则判断是否拥有lore
     * @param [item] 物品
     * @param [lore] 传说
     * @return [Boolean]
     */
    fun hasLore(item: ItemStack, lore: String? = null): Boolean {
        return item.hasLore(lore)
    }

    /**
     * 获取物品名
     * @param [item] 物品
     * @return [String]
     */
    fun getName(item: ItemStack): String {
        return getName(item, null)
    }

    /**
     * 获取物品名
     * 若物品没有自定义名称 且 player 不为空 则根据其使用的语言 获取 I18N 类型名
     * @param [item] 项
     * @param [player] 球员
     * @return [String]
     */
    fun getName(item: ItemStack, player: Player? = null): String {
        return item.getName(player)
    }

}
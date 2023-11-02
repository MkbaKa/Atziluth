package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector
import io.lumine.xikage.mythicmobs.util.MythicUtil

abstract class CustomLocationTargeter(
    mlc: MythicLineConfig
) : ILocationSelector(mlc) {

    private val xOffset = mlc.getPlaceholderDouble(arrayOf("xoffset", "xo", "x"), 0.0)
    private val yOffset = mlc.getPlaceholderDouble(arrayOf("yoffset", "yo", "y"), 0.0)
    private val zOffset = mlc.getPlaceholderDouble(arrayOf("zoffset", "zo", "z"), 0.0)
    private val forwardOffset = mlc.getPlaceholderDouble(arrayOf("forwardoffset", "foffset", "fo"), 0.0)
    private val sideOffset = mlc.getPlaceholderDouble(arrayOf("sideoffset", "soffset", "so"), 0.0)
    private val centered = mlc.getBoolean(arrayOf("blockCentered", "centered"), false)
    private val length = mlc.getPlaceholderDouble(arrayOf("length"), 0.0)

    override fun getLocations(meta: SkillMetadata): HashSet<AbstractLocation> {
        return hashSetOf()
    }

    fun mutateOverride(location: AbstractLocation, meta: SkillMetadata): AbstractLocation {
        return location.run {
            add(xOffset.get(meta), yOffset.get(meta), zOffset.get(meta))

            val len = length.get(meta)
            if (len != 0.0) {
                location.add(location.direction.clone().multiply(length.get(meta)))
            }

            if (centered) {
                x = blockX + 0.5
                z = blockZ + 0.5
            }

            MythicUtil.move(this, forwardOffset.get(meta), 0.0, sideOffset.get(meta))
        }
    }
}
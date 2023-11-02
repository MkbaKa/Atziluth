package me.mkbaka.atziluth.internal.module.hook.mythicmobs.impl.targeter4x

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation
import io.lumine.xikage.mythicmobs.io.MythicLineConfig
import io.lumine.xikage.mythicmobs.skills.SkillMetadata
import me.mkbaka.atziluth.internal.module.hook.mythicmobs.MythicAnnotations
import taboolib.common5.cbool
import taboolib.common5.cfloat

@MythicAnnotations.SkillTarget(["test-forward"])
class TestTarget(
    mlc: MythicLineConfig
) : CustomLocationTargeter(mlc) {

    private val forward = mlc.getPlaceholderDouble(arrayOf("forward", "f", "amount", "a"), 5.0)
    private val rotate = mlc.getPlaceholderDouble(arrayOf("rotate", "rot"), 0.0)
    private val useEyeLocation = mlc.getPlaceholderString(arrayOf("useeyelocation", "uel"), "false")
    private val lockPitch = mlc.getPlaceholderString(arrayOf("lockpitch"), "false")

    override fun getLocations(meta: SkillMetadata): HashSet<AbstractLocation> {
        val caster = meta.caster.entity
        val rotate = rotate.get(meta).cfloat

        val location = (if (useEyeLocation.get(meta).cbool) caster.eyeLocation else caster.location).clone()
        if (lockPitch.get(meta).cbool) location.pitch = 0.0F

        val direction = if (rotate != 0.0F) location.direction.rotate(rotate) else location.direction

        return hashSetOf(mutateOverride(location.add(direction.normalize().multiply(forward.get(meta))), meta))
    }

}
package me.mkbaka.atziluth.utils

import me.mkbaka.atziluth.Atziluth
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import java.util.*

object EntityUtil {

    val UUID.entity: Entity?
        get() = Bukkit.getEntity(this)

    val Entity.isLiving: Boolean
        get() = this is LivingEntity && isAlive

    val LivingEntity.isAlive: Boolean
        get() = isValid && !isDead

    fun UUID.getLivingEntity(): LivingEntity? {
        return if (entity?.isLiving == true) entity as LivingEntity else null
    }

    fun Metadatable.setMetadataEZ(key: String, value: Any) {
        this.setMetadata(key, FixedMetadataValue(Atziluth.plugin, value))
    }

    fun Metadatable.removeMetadataEZ(key: String) {
        this.removeMetadata(key, Atziluth.plugin)
    }

    fun LivingEntity.getMetadataEZ(key: String, def: Any): Any {
        return if (this.hasMetadata(key)) {
            this.getMetadata(key)[0]
        } else {
            setMetadataEZ(key, def)
            def
        }
    }
}
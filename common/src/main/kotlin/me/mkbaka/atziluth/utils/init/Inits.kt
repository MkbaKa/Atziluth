package me.mkbaka.atziluth.utils.init

import me.mkbaka.atziluth.utils.ClassUtil.instance
import taboolib.common.platform.function.warning

object Initializes {

    fun initClassFields(clazz: Class<*>) {
        clazz.fields.forEach { field ->
            if (!field.isAnnotationPresent(InitBy::class.java)) return@forEach

            field.isAccessible = true
            val initPath = field.getAnnotation(InitBy::class.java).path.ifEmpty { "${field.type.`package`.name}.impl.${field.type.simpleName}Impl" }

            val result = kotlin.runCatching {
                Class.forName(initPath).instance
            }

            if (result.isFailure || result.getOrNull() == null) {
                warning("无法从 $initPath 获取到字段 ${field.name} 的实现.")
                return@forEach
            }

            field.set(clazz.instance, result.getOrNull()!!)
        }
    }

}
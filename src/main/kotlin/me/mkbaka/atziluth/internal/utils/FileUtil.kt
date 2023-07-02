package me.mkbaka.atziluth.internal.utils

import taboolib.common.io.newFolder
import java.io.File

object FileUtil {

    fun File.executeSubFiles(callback: (file: File) -> Unit) {
        listFiles()?.forEach {
            if (it.isDirectory) it.executeSubFiles(callback) else callback(it)
        }
    }

    fun newFolder(file: File, folder: String, callback: (File) -> Unit): File {
        val result = newFolder(file, folder, create = false)
        if (!result.exists()) {
            result.mkdirs()
            callback(result)
        }
        return result
    }

}
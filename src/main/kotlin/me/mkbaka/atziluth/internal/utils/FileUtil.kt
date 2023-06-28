package me.mkbaka.atziluth.internal.utils

import java.io.File

object FileUtil {

    fun File.executeSubFiles(callback: (file: File) -> Unit) {
        listFiles()?.forEach {
            if (it.isDirectory) it.executeSubFiles(callback) else callback(it)
        }
    }

}
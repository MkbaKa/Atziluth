package me.mkbaka.atziluth.utils

import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.lang.Language.path
import java.io.File
import java.net.JarURLConnection
import java.net.URISyntaxException
import java.util.jar.JarFile

object FileUtil {

    /**
     * 对该目录下所有文件执行函数
     * @param [callback] 函数
     */
    fun File.executeSubFiles(callback: (File) -> Unit) {
        this.listFiles()?.forEach {  file ->
            if (file.isDirectory) file.executeSubFiles(callback) else callback(file)
        }
    }

    /**
     * 释放资源文件 若path为空则释放整个文件夹
     * @param [folder] 目录
     * @param [path] 资源文件
     */
    fun releaseResource(folder: File, path: String? = null) {
        if (!path.isNullOrEmpty()) releaseResourceFile("${folder.name}/$path") else releaseResources(folder.name)
    }

    /**
     * 释放目录下的所有资源文件
     * @param [folder] 目录
     * @author taboolib.common.io.Project1.kt#URL.getResources
     */
    fun releaseResources(folder: String) {
        val resource = this::class.java.classLoader.getResource(folder)!!
        val srcFile = try {
            File(resource.toURI())
        } catch (ex: IllegalArgumentException) {
            File((resource.openConnection() as JarURLConnection).jarFileURL.toURI())
        } catch (ex: URISyntaxException) {
            File(path)
        }
        JarFile(srcFile).stream().forEach { entry ->
            if (entry.name.startsWith(folder) && !entry.isDirectory) {
                releaseResourceFile(entry.name)
            }
        }
    }
}
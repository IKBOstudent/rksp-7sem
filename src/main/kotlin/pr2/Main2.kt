package pr2

import org.apache.commons.io.FileUtils
import utils.measureFuncTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.fileSize

fun copyUsingStreams(source: File, destination: File) {
    try {
        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(1024)
                var length: Int
                while (true) {
                    length = input.read(buffer)
                    if (length > 0) {
                        output.write(buffer, 0, length)
                    } else {
                        break
                    }
                }
            }
        }
    } catch (e: Exception) {
        println("Failed copy using streams, ${e.localizedMessage}")
        e.printStackTrace()
    }
}

fun copyUsingFileChannel(source: File, destination: File) {
    try {
        FileInputStream(source).channel.use { input ->
            FileOutputStream(destination).channel.use { output ->
                output.transferFrom(input, 0, input.size())
            }
        }
    } catch (e: Exception) {
        println("Failed copy using FileChannel, ${e.localizedMessage}")
        e.printStackTrace()
    }
}

fun copyUsingApacheCommons(source: File, destination: File) {
    try {
        FileUtils.copyFile(source, destination)
    } catch (e: Exception) {
        println("Failed copy using Apache Commons IO, ${e.localizedMessage}")
        e.printStackTrace()
    }
}

fun copyUsingFilesClass(source: File, destination: File) {
    try {
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING)
    } catch (e: Exception) {
        println("Failed copy using Files class, ${e.localizedMessage}")
        e.printStackTrace()
    }
}

fun main() {
    val source = File("source-100mb.bin")
    val dest1 = File("dest1.bin")
    val dest2 = File("dest2.bin")
    val dest3 = File("dest3.bin")
    val dest4 = File("dest4.bin")

    measureFuncTime("streams"){
        copyUsingStreams(source, dest1)
    }
    measureFuncTime("channel"){
        copyUsingFileChannel(source, dest2)
    }
    measureFuncTime("apache"){
        copyUsingApacheCommons(source, dest3)
    }
    measureFuncTime("files"){
        copyUsingFilesClass(source, dest4)
    }

    println("original size: ${source.toPath().fileSize()}")
    println("dest1 size: ${dest1.toPath().fileSize()}")
    println("dest2 size: ${dest2.toPath().fileSize()}")
    println("dest3 size: ${dest3.toPath().fileSize()}")
    println("dest4 size: ${dest4.toPath().fileSize()}")

}

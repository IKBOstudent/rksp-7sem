package pr2

import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path

fun getChecksum(file: Path): String? {
    try {
        var checksum = 0

        val bytes = Files.readAllBytes(file)
        val byteBuffer = ByteBuffer.wrap(bytes)

        val bitMask = 0xFFFF

        while (byteBuffer.hasRemaining()) {
            if (byteBuffer.remaining() > 1) {
                val value = byteBuffer.short.toInt() and bitMask
                checksum = (checksum + value) and bitMask
            } else {
                val lastByte = (byteBuffer.get().toInt() and 0xFF) shl 8
                checksum = (checksum + lastByte) and bitMask
            }
        }
        return checksum.toString(16)
    } catch (e: Exception) {
        println("Failed to get checksum, ${e.localizedMessage}")
        e.printStackTrace()
        return null
    }
}

fun main() {
    val path = Path.of("input.txt")
    println(getChecksum(path))
}

package pr2

import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val path = Path.of("input.txt")

    try {
        Files.readAllLines(path).forEach { println(it) }
    } catch (e: Exception) {
        println("Error: ${e.localizedMessage}")
    }

}

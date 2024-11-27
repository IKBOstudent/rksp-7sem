package pr2

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import kotlin.io.path.fileSize

fun watchDir(dir: Path) {
    val watchService = FileSystems.getDefault().newWatchService()
    dir.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_MODIFY,
        StandardWatchEventKinds.ENTRY_DELETE
    )

    println("Start watch")
    while (true) {
        val key = watchService.take()

        for (event in key.pollEvents()) {
            val kind = event.kind()
            val fileName = event.context() as Path
            val filePath = dir.resolve(fileName)

            when (kind) {
                StandardWatchEventKinds.ENTRY_CREATE -> {
                    println("File created: $fileName")
                }

                StandardWatchEventKinds.ENTRY_MODIFY -> {
                    println("File modified: $fileName")

                    if (Files.isReadable(filePath)) {
                        val newContent = Files.readAllLines(filePath)
                        println("File $fileName was modified. Current content:\n$newContent")
                        println("File new checksum ${getChecksum(filePath)}")
                    }
                }

                StandardWatchEventKinds.ENTRY_DELETE -> {
                    println("File deleted: $fileName")

                    try {
                        getChecksum(filePath)
                    } catch (e: Exception) {
                        println("file deleted, error")
                    }
                }
            }
        }

        val valid = key.reset()
        if (!valid) {
            break
        }
    }
}

fun main() {
    val dir = Path.of("watchDir")

    watchDir(dir)
}
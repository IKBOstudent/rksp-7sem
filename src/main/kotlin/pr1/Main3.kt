package pr1

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import kotlin.random.Random

enum class FileType {
    XML,
    JSON,
    XLS,
}

data class File(
    val type: FileType,
    val size: Int,
)

class FileGenerator(
    private val queue: BlockingQueue<File>,
) : Runnable {
    override fun run() {
        while (true) {
            Thread.sleep(Random.nextLong(100, 1000))
            val fileType = FileType.values().random()
            val fileSize = Random.nextInt(10, 100)
            val newFile = File(fileType, fileSize)
            println("Generated file: $newFile")
            queue.put(newFile)
        }
    }
}

class FileProcessor(
    private val queue: BlockingQueue<File>,
    private val fileType: FileType,
) : Runnable {
    override fun run() {
        println("FileProcessor $fileType active")
        val file = queue.take()
        if (file.type == fileType) {
            println("FileProcessor $fileType: processing $file")
            Thread.sleep(file.size * 7L)
            println("FileProcessor $fileType: processed $file")
        } else {
            println("FileProcessor $fileType: return file")
            queue.put(file)
        }
    }
}

fun main() {
    val queue: BlockingQueue<File> = ArrayBlockingQueue(5)

    val generatorThread = Thread(FileGenerator(queue))
    generatorThread.start()

    val executorService = Executors.newFixedThreadPool(FileType.values().size)

    Executors.newSingleThreadExecutor().submit {
        while (true) {
            println(executorService.toString())
            if (queue.isNotEmpty()) {
                FileType.values().forEach { fileType ->
                    executorService.submit(
                        FileProcessor(queue, fileType),
                    )
                }
            }
            Thread.sleep(100)
        }
    }

    // executorService.shutdown()
}

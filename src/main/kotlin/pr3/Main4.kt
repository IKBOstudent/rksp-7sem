package pr3

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

enum class FileType {
    XML,
    JSON,
    XLS,
}

data class File(
    val type: FileType,
    val size: Int,
)

fun fileGenerator(): Observable<File> {
    return Observable.create<File> { emitter ->
        while (!emitter.isDisposed) {
            val fileType = FileType.entries.random()
            val fileSize = (10..100).random()
            val sleepTime = (100..1000).random()

            Thread.sleep(sleepTime.toLong())

            emitter.onNext(File(fileType, fileSize))
        }
    }.subscribeOn(Schedulers.computation())
}

fun fileHandler(files: Observable<List<File>>, fileType: FileType) {
    files
        .flatMap { fileList -> Observable.fromIterable(fileList) }
        .filter { it.type == fileType }
        .flatMap { Observable.just(it).delay(7 * it.size.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS) }
        .subscribe { println("Processed file: $it") }
}

fun main() {
    val fileObservable = fileGenerator()
    val queuedFiles = fileObservable.buffer(5)

    fileHandler(queuedFiles, FileType.XML)
    fileHandler(queuedFiles, FileType.JSON)
    fileHandler(queuedFiles, FileType.XLS)

    Thread.sleep(10000)
}




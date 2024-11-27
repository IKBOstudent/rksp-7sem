package pr1

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.Future
import java.util.concurrent.RecursiveTask
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun dummy(array: IntArray): Int {
    var sum = 0
    for (element in array) {
        Thread.sleep(1)
        sum += element
    }
    return sum
}

fun threaded(array: IntArray): Int {
    val numThreads = 10
    val chunkSize = array.size / numThreads
    val executor = Executors.newFixedThreadPool(numThreads)
    println("Created pool of $numThreads threads")
    val futures = mutableListOf<Future<Int>>()

    for (i in 0..<numThreads) {
        val start = chunkSize * i
        val end = min(array.size, chunkSize * (i + 1))
        val operationFuture =
            executor.submit(
                Callable {
                    var sum = 0
                    for (j in start..<end) {
                        Thread.sleep(1)
                        sum += array[j]
                    }
                    sum
                },
            )

        futures.add(operationFuture)
    }

    var totalSum = 0
    for (future in futures) {
        totalSum += future.get()
    }

    executor.shutdown()
    return totalSum
}

class SumTask(
    private val array: IntArray,
    private val start: Int,
    private val end: Int,
) : RecursiveTask<Int>() {
    private val threshold = 1001

    override fun compute(): Int {
        if (end - start <= threshold) {
            var sum = 0
            for (i in start..<end) {
                Thread.sleep(1)
                sum += array[i]
            }
            return sum
        } else {
            val mid = (start + end) / 2
            val leftTask = SumTask(array, start, mid)
            val rightTask = SumTask(array, mid, end)

            leftTask.fork()
            val rightResult = rightTask.compute()
            val leftResult = leftTask.join()
            return leftResult + rightResult
        }
    }
}

fun forkJoinSum(array: IntArray): Int {
    val pool: ForkJoinPool = ForkJoinPool.commonPool()
    return pool.invoke(SumTask(array, 0, array.size))
}

fun main() {
    val array = IntArray(100000) { it }

    val dummyTime =
        measureTimeMillis {
            println("dummy Sum: ${dummy(array)}")
        }

    val threadedTime =
        measureTimeMillis {
            println("Threaded Sum: ${threaded(array)}")
        }

    val forkJoinTime =
        measureTimeMillis {
            println("ForkJoin Sum: ${forkJoinSum(array)}")
        }

    println("Dummy Time: $dummyTime ms")
    println("Threaded Time: $threadedTime ms")
    println("ForkJoin Time: $forkJoinTime ms")
}

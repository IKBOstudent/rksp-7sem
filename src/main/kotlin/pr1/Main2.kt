package pr1

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.random.Random

fun main() {
    val executor = Executors.newFixedThreadPool(4)

    while (true) {
        println("Enter num")
        val input = readlnOrNull()

        val number = input?.toIntOrNull()

        if (number != null) {
            val future =
                executor.submit(
                    Callable {
                        square(number)
                    },
                )
            executor.submit {
                val result = future.get()
                println(result)
            }
        }
    }
}

fun square(number: Int): Int {
    Thread.sleep(Random.nextLong(1000, 5000))
    return number * number
}

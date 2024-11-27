package utils

import kotlin.system.measureTimeMillis

fun measureFuncTime(name: String, block: () -> Unit) {
    val time = measureTimeMillis {
        println("$name result: ${block()}")
    }
    println("$name time: $time ms")
}

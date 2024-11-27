package pr3

import io.reactivex.rxjava3.core.Observable

fun randomStreamFilter(): Observable<Int> {
    val randomNumbersStream = Observable.create<Int> { emitter ->
        repeat(1000) {
            emitter.onNext((0..1000).random())
        }
        emitter.onComplete()
    }

    return randomNumbersStream.filter { it > 500 }
}

fun randomStreamConcat(): Observable<Int> {
    val stream1 = Observable.create<Int> { emitter ->
        repeat(10) {
            val next = (0..9).random()
            print("[s1 $next] ")
            emitter.onNext(next)
        }
        emitter.onComplete()
    }

    val stream2 = Observable.create<Int> { emitter ->
        repeat(10) {
            val next = (0..9).random()
            print("[s2 $next] ")
            emitter.onNext(next)
        }
        emitter.onComplete()
    }

    return Observable.concat(stream1, stream2)
}

fun randomStreamHead(): Observable<Int> {
    val randomTenNumbersStream = Observable.create<Int> { emitter ->
        repeat(10) {
            val next = (0..9).random()
            print("[s $next] ")
            emitter.onNext(next)
        }
        emitter.onComplete()
    }

    return randomTenNumbersStream.take(5)
}

fun main() {
    println("filter > 500")
    randomStreamFilter().subscribe { print("$it, ") }

    println("\n\nconcat streams")
    randomStreamConcat().subscribe { print("$it, ") }

    println("\n\ntake 5 from head")
    randomStreamHead().subscribe { print("$it, ") }
}

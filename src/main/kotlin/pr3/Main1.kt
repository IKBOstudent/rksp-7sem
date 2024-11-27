package pr3

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun getTempSensor(): Observable<Int> {
    return Observable.interval(1, TimeUnit.SECONDS)
        .map { (15..30).random() }
}

fun getCO2Sensor(): Observable<Int> {
    return Observable.interval(1, TimeUnit.SECONDS)
        .map { (30..100).random() }
}

fun main() {
    val disposables = CompositeDisposable()

    val temperatureSensor = getTempSensor()
    val co2Sensor = getCO2Sensor()

    val combinedSensors = Observable.combineLatest(
        temperatureSensor, co2Sensor,
        { temperature, co2 -> Pair(temperature, co2) }
    )

    val subscription = combinedSensors
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.single())
        .subscribe { (temperature, co2) ->
            println("Sensors: temp $temperature, CO2 $co2")

            val temperatureExceeded = temperature > 25
            val co2Exceeded = co2 > 70

            when {
                temperatureExceeded && co2Exceeded -> println("ALARM!!! Temperature: $temperature, CO2: $co2")
                temperatureExceeded -> println("Temperature warning: $temperature")
                co2Exceeded -> println("CO2 warning: $co2")
            }
        }

    disposables.add(subscription)
    Thread.sleep(10000)
    disposables.dispose()
}
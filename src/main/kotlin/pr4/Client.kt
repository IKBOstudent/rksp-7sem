package pr4

import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload

class Client {
    private val client = RSocketConnector.connectWith(TcpClientTransport.create(7000)).block()!!

    fun getBalance(userId: String) {
        client.requestResponse(DefaultPayload.create(userId))
            .map { it.dataUtf8 }
            .subscribe { println("Response balance: $it") }
    }

    fun getTransactions(userId: String) {
        client.requestStream(DefaultPayload.create(userId))
            .map { it.dataUtf8 }
            .subscribe { println("Stream item: $it") }
    }

    fun transaction(userId: String) {
        client.fireAndForget(DefaultPayload.create(userId))
            .subscribe()
    }
}

fun main() {
    val client = Client()

    client.getBalance("user1")
    client.getTransactions("user1")
    client.transaction("user1")
}
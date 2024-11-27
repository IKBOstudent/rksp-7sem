package pr4

import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.TcpServerTransport
import io.rsocket.util.DefaultPayload
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class Server {
    init {
        val server = RSocketServer.create { _, _ ->
            Mono.just(object : RSocket {
                override fun requestResponse(payload: Payload): Mono<Payload> {
                    val userId = payload.dataUtf8
                    val balance = "Balance for user $userId is 1000"
                    return Mono.just(DefaultPayload.create(balance))
                }

                override fun requestStream(payload: Payload): Flux<Payload> {
                    return Flux.just(
                        "Tx1 for user",
                        "Tx2 for user",
                        "Tx3 for user"
                    ).map { DefaultPayload.create(it) }
                }

                override fun fireAndForget(payload: Payload): Mono<Void> {
                    println("Transaction notification received for user: ${payload.dataUtf8}")
                    return Mono.empty()
                }
            })
        }

        server.bind(TcpServerTransport.create(7000)).block()
    }
}

fun main() {
    Server()
}
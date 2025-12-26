package raven

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.builtins.ListSerializer

class RemoteOutbox<P>(private val options: RemoteOutboxOptions<P>) : Outbox<P> {
    override suspend fun store(params: P): P {
        val json = options.http.post(options.endpoint.store()) {
            setBody(options.codec.encodeToString(options.serializer, params))
        }.bodyAsText()
        return options.codec.decodeFromString(options.serializer, json)
    }

    override suspend fun sent(to: String): List<P> {
        val serializer = ListSerializer(options.serializer)
        val json = options.http.get(options.endpoint.sent(to)).bodyAsText()
        return options.codec.decodeFromString(serializer, json)
    }

    override suspend fun delete(receiver: String): List<P> {
        val serializer = ListSerializer(options.serializer)
        val json = options.http.delete(options.endpoint.delete(receiver)).bodyAsText()
        return options.codec.decodeFromString(serializer, json)
    }
}
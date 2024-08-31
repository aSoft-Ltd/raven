package raven

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import koncurrent.later
import kotlinx.serialization.builtins.ListSerializer

class RemoteOutbox<P>(private val options: RemoteOutboxOptions<P>) : Outbox<P> {
    override fun store(params: P) = options.scope.later {
        val json = options.http.post(options.endpoint.store()) {
            setBody(options.codec.encodeToString(options.serializer, params))
        }.bodyAsText()
        options.codec.decodeFromString(options.serializer, json)
    }

    override fun sent(to: String) = options.scope.later {
        val serializer = ListSerializer(options.serializer)
        val json = options.http.get(options.endpoint.sent(to)).bodyAsText()
        options.codec.decodeFromString(serializer, json)
    }

    override fun delete(receiver: String) = options.scope.later {
        val serializer = ListSerializer(options.serializer)
        val json = options.http.delete(options.endpoint.delete(receiver)).bodyAsText()
        options.codec.decodeFromString(serializer, json)
    }
}
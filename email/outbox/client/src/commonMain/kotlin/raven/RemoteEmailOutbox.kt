package raven

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import koncurrent.later
import kotlinx.serialization.builtins.ListSerializer

class RemoteEmailOutbox(private val options: RemoteEmailOutboxOptions) : EmailOutbox {
    override fun store(params: SendEmailParams) = options.scope.later {
        val serializer = SendEmailParams.serializer()
        val json = options.http.post(options.endpoint.store()) {
            setBody(options.codec.encodeToString(serializer, params))
        }.bodyAsText()
        options.codec.decodeFromString(serializer, json)
    }

    override fun sent(to: String) = options.scope.later {
        val serializer = ListSerializer(SendEmailParams.serializer())
        val json = options.http.get(options.endpoint.sent(to)).bodyAsText()
        options.codec.decodeFromString(serializer, json)
    }

    override fun delete(receiver: String) = options.scope.later {
        val serializer = ListSerializer(SendEmailParams.serializer())
        val json = options.http.delete(options.endpoint.delete(receiver)).bodyAsText()
        options.codec.decodeFromString(serializer, json)
    }
}
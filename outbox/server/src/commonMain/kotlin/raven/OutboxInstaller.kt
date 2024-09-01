package raven

import io.ktor.server.request.receiveText
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.util.getValue
import koncurrent.later.await
import kotlinx.serialization.builtins.ListSerializer

fun <P> Routing.installOutbox(controller: OutboxController<P>) {
    post(controller.endpoint.store()) {
        val json = call.receiveText()
        val params = controller.codec.decodeFromString(controller.serializer, json)
        val result = controller.service.store(params).await()
        controller.codec.encodeToString(controller.serializer, result)
    }

    get(controller.endpoint.sent("{to}")) {
        val to: String by call.pathParameters
        val messages = try {
            controller.service.sent(to).await()
        } catch (err: Throwable) {
            emptyList()
        }
        val serializer = ListSerializer(controller.serializer)
        call.respondText(controller.codec.encodeToString(serializer, messages))
    }

    delete(controller.endpoint.delete("{receiver}")) {
        val receiver: String by call.pathParameters
        val messages = try {
            controller.service.delete(receiver).await()
        } catch (err: Throwable) {
            emptyList()
        }
        val serializer = ListSerializer(controller.serializer)
        call.respondText(controller.codec.encodeToString(serializer, messages))
    }
}
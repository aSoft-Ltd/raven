package raven

import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.util.getValue
import kotlinx.coroutines.future.await
import kotlinx.serialization.builtins.ListSerializer

fun Routing.installOutbox(controller: OutboxController) {
    get(controller.endpoint.sent("{to}")) {
        val to: String by call.pathParameters
        val messages = try {
            controller.service.sent(to).await()
        } catch (err: Throwable) {
            emptyList()
        }
        val serializer = ListSerializer(SendEmailParams.serializer())
        call.respondText(controller.codec.encodeToString(serializer, messages))
    }

    delete(controller.endpoint.delete("{receiver}")) {
        val receiver: String by call.pathParameters
        val messages = try {
            controller.service.delete(receiver).await()
        } catch (err: Throwable) {
            emptyList()
        }
        val serializer = ListSerializer(SendEmailParams.serializer())
        call.respondText(controller.codec.encodeToString(serializer, messages))
    }
}
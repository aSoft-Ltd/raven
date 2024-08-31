package raven.internal

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import raven.KilaKonaOptions
import raven.KilaKonaSmsAgentException
import raven.SendSmsParams

internal fun HttpRequestBuilder.headers(options: KilaKonaOptions) = headers {
    accept(ContentType.Application.Json)
    contentType(ContentType.Application.Json)
    header("api_key", options.key)
    header("api_secret", options.secret)
}

internal fun SendSmsParams.toJsonObject() = buildJsonObject {
    put("senderId", from)
    put("messageType", "text")
    put("message", body)
    put("contacts", to.joinToString(separator = ","))
}

internal fun JsonObject.ensureSuccess() : JsonObject {
    val success = this["success"]?.jsonPrimitive?.booleanOrNull == true
    if (!success) {
        val message = this["message"]?.jsonPrimitive?.content
        throw KilaKonaSmsAgentException(message)
    }

    return this["data"]?.jsonObject ?: throw KilaKonaSmsAgentException(
        message = "Couldn't retrieve data information even though the request was successful"
    )
}
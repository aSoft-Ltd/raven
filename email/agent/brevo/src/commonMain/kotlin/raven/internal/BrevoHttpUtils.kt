package raven.internal

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import raven.Address
import raven.BrevoOptions
import raven.SendEmailParams


internal fun HttpRequestBuilder.headers(options: BrevoOptions) = headers {
    accept(ContentType.Application.Json)
    contentType(ContentType.Application.Json)
    header("api-key", options.key)
}

internal fun SendEmailParams.toJsonObject() = buildJsonObject {
    put("sender", from.toJsonObject())
    put("to", toReceivers())
    put("subject", subject)
    put("htmlContent", body)
}

private fun SendEmailParams.toReceivers() = buildJsonArray {
    for (receiver in to) add(receiver.toJsonObject())
}

private fun Address.toJsonObject() = buildJsonObject {
    if (name != null) put("name", name)
    put("email", email)
}
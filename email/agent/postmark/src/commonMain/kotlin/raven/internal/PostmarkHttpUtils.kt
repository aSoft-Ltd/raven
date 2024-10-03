@file:OptIn(ExperimentalEncodingApi::class)

package raven.internal

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import koncurrent.later.await
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import raven.Address
import raven.EmbeddedResource
import raven.PostmarkOptions
import raven.SendEmailParams
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


internal fun HttpRequestBuilder.headers(options: PostmarkOptions) = headers {
    accept(ContentType.Application.Json)
    contentType(ContentType.Application.Json)
    append("X-Postmark-Server-Token", options.token)
}

internal suspend fun SendEmailParams.toJson() = buildJsonObject {
    put("From", from.toJsonElement())
    put("To", to.toJsonElement())
    if (cc.isNotEmpty()) put("Cc", cc.toJsonElement())
    if (bcc.isNotEmpty()) put("Bcc", bcc.toJsonElement())
    put("Subject", JsonPrimitive(subject))
    put("HtmlBody", JsonPrimitive(body))
    put("Attachments", buildJsonArray {
        for (r in inline) add(r.toResources())
        for (r in attachments) add(r.toAttachment())
    })
}

private suspend fun JsonObjectBuilder.put(resource: EmbeddedResource) {
    put("Name", JsonPrimitive(resource.name))
    put("Content", JsonPrimitive(Base64.encode(resource.read().await())))
    put("ContentType", JsonPrimitive(resource.type))
}

private suspend fun EmbeddedResource.toResources() = buildJsonObject {
    put(this@toResources)
    put("ContentID", JsonPrimitive("cid:$id"))
}

private suspend fun EmbeddedResource.toAttachment() = buildJsonObject { put(this@toAttachment) }

private fun Address.toJsonElement(): JsonElement {
    val n = name ?: email.split("@").first()
    return JsonPrimitive("$n <$email>")
}

private fun List<Address>.toJsonElement() = JsonPrimitive(joinToString(",") { it.toJsonElement().jsonPrimitive.content })
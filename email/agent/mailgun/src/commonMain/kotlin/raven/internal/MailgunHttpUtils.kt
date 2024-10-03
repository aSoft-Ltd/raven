@file:OptIn(ExperimentalEncodingApi::class)

package raven.internal

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.escapeIfNeeded
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket
import koncurrent.later.await
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import raven.Address
import raven.EmbeddedResource
import raven.MailgunOptions
import raven.SendEmailParams
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random


internal fun HttpRequestBuilder.headers(options: MailgunOptions) = headers {
    accept(ContentType.Application.Json)
    contentType(ContentType.Application.Json)
    basicAuth("api", options.key)
}

private suspend fun EmbeddedResource.toInlinePartData(): PartData {
    val data = read().await()
    return PartData.BinaryItem(
        provider = { ByteReadPacket(data) },
        dispose = {},
        partHeaders = headers {
            append("Content-Type", type)
            append("Filename", """"$name"""")
            append("Content-ID", id)
            append("Content-Disposition", "inline")
        }
    )
}

private suspend fun EmbeddedResource.toAttachmentPartData(): PartData {
    val data = read().await()
    return PartData.FileItem(
        provider = { ByteReadChannel(data) },
        dispose = {},
        partHeaders = headers {
            append("Content-Type", type)
            append("filename", """"$name"""")
            append("Content-Disposition", """attachment; filename="$name"""")
        }
    )
}

private fun boundary() = buildString {
    repeat(32) {
        append(Random.nextInt().toString(16))
    }
}.take(70)

private fun MultiPartMixedContent(parts: List<PartData>): MultiPartFormDataContent {
    val boundary = boundary()
    val contentType = ContentType.MultiPart.FormData.withParameter("boundary", boundary)
    return MultiPartFormDataContent(parts, boundary, contentType)
}

private suspend fun EmbeddedResource.toJson(name: String) = mapOf(
    "name" to name,
    "content" to Base64.encode(read().await()),
    "filename" to this.name
).entries.joinToString(";", prefix = "{", postfix = "}") { (key, value) -> """"$key": "$value"""" }

internal suspend fun SendEmailParams.toGptMultiPartFormData(): MultiPartFormDataContent {
    val others = multipart {
        for (resource in inline) append(
            key = "inline",
            value = resource.read().await(),
            headers = Headers.build {
                append(HttpHeaders.ContentDisposition, "inline")
                append(HttpHeaders.ContentType, resource.type)
                append("Content-ID", resource.id)
            }
        )
        for (attachment in attachments) append(
            key = "attachment",
            value = attachment.read().await(),
            headers = Headers.build {
                append(HttpHeaders.ContentDisposition, """attachment; filename="${attachment.name}"""")
                append(HttpHeaders.ContentType, attachment.type)
            }
        )
        append("from", from.toFormValue())
        add("to", to)
        append("subject", subject)
        append("html", body)
        add("cc", cc)
        add("bcc", bcc)
    }
    val parts = buildList<PartData> {
        for (attachment in attachments) appendAttachment(attachment)
        appendFormData("from", from.toFormValue())
        add("to", to)
        appendFormData("subject", subject)
        appendFormData("html", body)
        add("cc", cc)
        add("bcc", bcc)
    }
    return MultiPartMixedContent(others)
//    return MultiPartMixedContent(parts)
}

private suspend fun MutableList<PartData>.appendAttachment(attachment: EmbeddedResource) {
    val data = attachment.read().await()
    add(PartData.FormItem(
        value = Base64.encode(data),
//        provider = { ByteReadChannel(data) },
        dispose = {},
        partHeaders = Headers.build {
//            append(HttpHeaders.ContentDisposition, """form-data; name=attachment; attachment; filename="${attachment.name}"""")
            append(HttpHeaders.ContentDisposition, """form-data; name=attachment; filename=${attachment.name}""")
//            append(HttpHeaders.ContentDisposition, """attachment; filename="${attachment.name}"""")
//            append(HttpHeaders.ContentDisposition, """attachment; filename="${attachment.name}"""")
            append(HttpHeaders.ContentType, attachment.type)
            append(HttpHeaders.ContentLength, "${data.size}")
            append("Content-Transfer-Encoding", "base64")
            append("Mime-Version", "1.0")
        }
    ))
}

private fun MutableList<PartData>.appendFormData(key: String, value: String) {
    add(PartData.FormItem(value, {}, Headers.build {
        append(HttpHeaders.ContentDisposition, "form-data; name=${key.escapeIfNeeded()}")
    }))
}

private fun PartBuilder.add(key: String, recipients: List<Address>) {
    if (recipients.isEmpty()) return
    append(key, recipients.joinToString(",") { it.toFormValue() })
}

private fun MutableList<PartData>.add(key: String, recipients: List<Address>) {
    if (recipients.isEmpty()) return
    appendFormData(key, recipients.joinToString(",") { it.toFormValue() })
}

private fun Address.toFormValue(): String {
    val n = name ?: email.split("@")[0]
    return "$n <$email>"
}

private fun Address.toJsonObject() = buildJsonObject {
    if (name != null) put("name", name)
    put("email", email)
}
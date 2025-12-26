package raven.internal

import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.utils.io.ByteChannel
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import raven.Address
import raven.EmailAgent
import raven.EmailContentType
import raven.MailgunEmailAgentException
import raven.MailgunOptions
import raven.SendEmailParams

@PublishedApi
internal class MailgunEmailAgentImpl(
    private val options: MailgunOptions,
) : EmailAgent {

    override suspend fun canSend(count: Int): Boolean = credit() >= count

    override suspend fun credit(): Int {
        val json = options.http.get(options.endpoint.account()) {
            headers(options)
        }.bodyAsText()
        val resp = options.codec.decodeFromString(JsonObject.serializer(), json).ensureNoError()
        val limit = resp["limit"]?.jsonPrimitive?.intOrNull ?: throw MailgunEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")
        val current = resp["current"]?.jsonPrimitive?.intOrNull ?: throw MailgunEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")

        return limit - current
    }

    private fun JsonObject.ensureNoError(): JsonObject {
        val error = this["Error"]?.jsonPrimitive?.content
        if (error != null) throw MailgunEmailAgentException(error)
        return this
    }

    override fun supports(body: EmailContentType): Boolean = true

    private fun SendEmailParams.toWarningParams(credit: Int) = SendEmailParams(
        from = from,
        to = options.warning.to.map { Address(email = it) },
        subject = "Mailgun email credit below ${options.warning.limit}",
        body = options.warning.message(credit)
    )

    override suspend fun send(params: SendEmailParams): SendEmailParams {
        var credit = credit()
        val warning = options.warning
        if (credit > warning.to.size && credit <= warning.limit && warning.to.isNotEmpty()) {
            execute(params.toWarningParams(credit))
            credit--
        }

        if (credit <= params.to.size) {
            throw MailgunEmailAgentException("Out of credit, hence we can't send ${params.to.size} emails on a $credit credit")
        }
        return execute(params)
    }

    private fun SendEmailParams.domain() = from.email.split("@")[1]

    private suspend fun execute(params: SendEmailParams): SendEmailParams {
        val serializer = JsonObject.serializer()
        val json = options.http.post(options.endpoint.email(params.domain())) {
            accept(ContentType.MultiPart.FormData)
            headers(options)
//            val data = params.toMultiPartFormData()
            val data = params.toGptMultiPartFormData()
            val reader = ByteChannel()
            data.writeTo(reader)
            setBody(data)
        }.bodyAsText()
        val resp = options.codec.decodeFromString(serializer, json).ensureNoError()
        if (resp["id"] == null) {
            throw MailgunEmailAgentException(resp["message"]?.jsonPrimitive?.content)
        }
        return options.outbox?.store(params) ?: params
    }
}
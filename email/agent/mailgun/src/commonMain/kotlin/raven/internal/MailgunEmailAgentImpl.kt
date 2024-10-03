package raven.internal

import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.readBuffer
import io.ktor.utils.io.readText
import koncurrent.Later
import koncurrent.later
import koncurrent.later.await
import koncurrent.later.catch
import koncurrent.later.then
import kotlinx.coroutines.launch
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

    override fun canSend(count: Int): Later<Boolean> = credit().then { it > count }.catch { false }

    override fun credit(): Later<Int> = options.scope.later {
        val json = options.http.get(options.endpoint.account()) {
            headers(options)
        }.bodyAsText()
        val resp = options.codec.decodeFromString(JsonObject.serializer(), json).ensureNoError()
        val limit = resp["limit"]?.jsonPrimitive?.intOrNull ?: throw MailgunEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")
        val current = resp["current"]?.jsonPrimitive?.intOrNull ?: throw MailgunEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")

        limit - current
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

    override fun send(params: SendEmailParams): Later<SendEmailParams> = options.scope.later {
        var credit = credit().await()
        val warning = options.warning
        if (credit > warning.to.size && credit <= warning.limit && warning.to.isNotEmpty()) {
            execute(params.toWarningParams(credit)).await()
            credit--
        }

        if (credit <= params.to.size) {
            throw MailgunEmailAgentException("Out of credit, hence we can't send ${params.to.size} emails on a $credit credit")
        }
        execute(params).await()
    }

    private fun SendEmailParams.domain() = from.email.split("@")[1]

    private fun execute(params: SendEmailParams) = options.scope.later {
        val serializer = JsonObject.serializer()
        val json = options.http.post(options.endpoint.email(params.domain())) {
            accept(ContentType.MultiPart.FormData)
            headers(options)
//            val data = params.toMultiPartFormData()
            val data = params.toGptMultiPartFormData()
            val reader = ByteChannel()
            launch {
                data.writeTo(reader)
            }
            launch {
                println(reader.readBuffer().readText())
            }
            setBody(data)
        }.bodyAsText()
        val resp = options.codec.decodeFromString(serializer, json).ensureNoError()
        if (resp["id"] == null) {
            throw MailgunEmailAgentException(resp["message"]?.jsonPrimitive?.content)
        }
        options.outbox?.store(params)?.await() ?: params
    }
}
package raven.internal

import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import raven.Address
import raven.EmailAgent
import raven.EmailContentType
import raven.PostmarkEmailAgentException
import raven.PostmarkOptions
import raven.SendEmailParams

@PublishedApi
internal class PostmarkEmailAgentImpl(
    private val options: PostmarkOptions,
) : EmailAgent {

    override suspend fun canSend(count: Int): Boolean = credit() > count

    override suspend fun credit(): Int {
//        val json = options.http.get(options.endpoint.account()) {
//            headers(options)
//        }.bodyAsText()
//        val resp = options.codec.decodeFromString(JsonObject.serializer(), json).ensureNoError()
//        val limit = resp["limit"]?.jsonPrimitive?.intOrNull ?: throw PostmarkEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")
//        val current = resp["current"]?.jsonPrimitive?.intOrNull ?: throw PostmarkEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")
//
//        limit - current
        return 1000
    }

    private fun JsonObject.ensureNoError(): JsonObject {
        val error = this["Error"]?.jsonPrimitive?.content
        if (error != null) throw PostmarkEmailAgentException(error)
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
            throw PostmarkEmailAgentException("Out of credit, hence we can't send ${params.to.size} emails on a $credit credit")
        }
        return execute(params)
    }

    private suspend fun execute(params: SendEmailParams) : SendEmailParams {
        val serializer = JsonObject.serializer()
        val json = options.http.post(options.endpoint.email()) {
            accept(ContentType.MultiPart.FormData)
            headers(options)
            setBody(options.codec.encodeToString(JsonObject.serializer(), params.toJson()))
        }.bodyAsText()

        println(json)
        val resp = options.codec.decodeFromString(serializer, json).ensureNoError()
        if (resp["ErrorCode"]?.jsonPrimitive?.intOrNull != 0) {
            throw PostmarkEmailAgentException(resp["Message"]?.jsonPrimitive?.content)
        }
        return options.outbox?.store(params) ?: params
    }
}
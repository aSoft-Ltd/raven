package raven.internal

import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import koncurrent.Later
import koncurrent.later
import koncurrent.later.await
import koncurrent.later.catch
import koncurrent.later.then
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

    override fun canSend(count: Int): Later<Boolean> = credit().then { it > count }.catch { false }

    override fun credit(): Later<Int> = options.scope.later {
//        val json = options.http.get(options.endpoint.account()) {
//            headers(options)
//        }.bodyAsText()
//        val resp = options.codec.decodeFromString(JsonObject.serializer(), json).ensureNoError()
//        val limit = resp["limit"]?.jsonPrimitive?.intOrNull ?: throw PostmarkEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")
//        val current = resp["current"]?.jsonPrimitive?.intOrNull ?: throw PostmarkEmailAgentException("Limit not set. Visit https://app.mailgun.com/app/account/settings to set limit")
//
//        limit - current
        1000
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

    override fun send(params: SendEmailParams): Later<SendEmailParams> = options.scope.later {
        var credit = credit().await()
        val warning = options.warning
        if (credit > warning.to.size && credit <= warning.limit && warning.to.isNotEmpty()) {
            execute(params.toWarningParams(credit)).await()
            credit--
        }

        if (credit <= params.to.size) {
            throw PostmarkEmailAgentException("Out of credit, hence we can't send ${params.to.size} emails on a $credit credit")
        }
        execute(params).await()
    }

    private fun execute(params: SendEmailParams) = options.scope.later {
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
        options.outbox?.store(params)?.await() ?: params
    }
}
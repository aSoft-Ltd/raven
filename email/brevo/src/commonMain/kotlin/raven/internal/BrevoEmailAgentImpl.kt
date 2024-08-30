package raven.internal

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import koncurrent.Later
import koncurrent.later
import koncurrent.later.await
import koncurrent.later.catch
import koncurrent.later.then
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import raven.Address
import raven.BrevoOptions
import raven.BrevoEmailAgentException
import raven.EmailContentType
import raven.EmailAgent
import raven.SendEmailParams

@PublishedApi
internal class BrevoEmailAgentImpl(
    private val options: BrevoOptions,
) : EmailAgent {

    override fun canSend(count: Int): Later<Boolean> = credit().then { it > count }.catch { false }

    override fun credit(): Later<Int> = options.scope.later {
        val json = options.http.get(options.endpoint.account) {
            headers(options)
        }.bodyAsText()

        val resp = options.codec.decodeFromString(JsonObject.serializer(), json)

        val plans = resp["plan"]?.jsonArray ?: throw BrevoEmailAgentException("Couldn't fetch account information")

        val email = plans.filterIsInstance<JsonObject>().filterNot {
            it["type"]?.jsonPrimitive?.content == "sms"
        }.firstOrNull() ?: throw BrevoEmailAgentException(
            message = "Couldn't obtain plan information, there is a change you moved from a free plan to a paid plan just check with Brevo"
        )

        email["credits"]?.jsonPrimitive?.intOrNull ?: throw BrevoEmailAgentException(
            message = "Could not get credit information even though ${email["type"]?.jsonPrimitive?.content} plan was deduced"
        )
    }

    override fun supports(body: EmailContentType): Boolean = true

    override fun send(params: SendEmailParams): Later<SendEmailParams> = options.scope.later {
        var credit = credit().await()
        val warning = options.warning
        if (credit > warning.to.size && credit <= warning.limit && warning.to.isNotEmpty()) {
            val p = SendEmailParams(
                from = params.from,
                to = warning.to.map { Address(email = it) },
                subject = "Brevo email credit below ${warning.limit}",
                body = warning.message(credit)
            )
            execute(p).await()
            credit--
        }

        if (credit <= params.to.size) {
            throw BrevoEmailAgentException("Out of credit, hence we can't send ${params.to.size} emails on a $credit credit")
        }
        execute(params).await()
    }

    private fun execute(params: SendEmailParams) = options.scope.later {
        val serializer = JsonObject.serializer()
        val json = options.http.post(options.endpoint.sms) {
            headers(options)
            setBody(options.codec.encodeToString(serializer, params.toJsonObject()))
        }.bodyAsText()
        val resp = options.codec.decodeFromString(serializer, json)
        if (resp["messageId"] == null) {
            throw BrevoEmailAgentException(resp["message"]?.jsonPrimitive?.content)
        }
        params
    }
}
package raven.internal

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import koncurrent.Later
import koncurrent.later
import koncurrent.later.await
import koncurrent.later.catch
import koncurrent.later.then
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import raven.Address
import raven.BrevoOptions
import raven.BrevoServiceException
import raven.EmailContentType
import raven.EmailSender
import raven.SendEmailParams

internal class BrevoEmailSender(
    private val options: BrevoOptions,
    private val service: BrevoEmailServiceImpl
) : EmailSender {

    override fun canSend(count: Int): Later<Boolean> = service.account.credit().then {
        it > count
    }.catch { false }

    override fun supports(body: EmailContentType): Boolean = true

    override fun send(params: SendEmailParams): Later<SendEmailParams> = options.scope.later {
        var credit = service.account.credit().await()
        if (credit > 0 && credit <= options.warning.limit) {
            val p = SendEmailParams(
                from = params.from,
                to = Address(email = options.warning.to),
                subject = "Brevo email credit below ${options.warning.limit}",
                body = options.warning.message(credit)
            )
            execute(p).await()
            credit--
        }

        if (credit <= params.to.size) {
            throw BrevoServiceException("Out of credit, hence we can't send ${params.to.size}")
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
            throw BrevoServiceException(resp["message"]?.jsonPrimitive?.content)
        }
        params
    }
}
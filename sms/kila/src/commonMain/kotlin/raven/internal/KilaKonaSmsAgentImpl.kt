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
import kotlinx.serialization.json.jsonPrimitive
import raven.KilaKonaOptions
import raven.KilaKonaSmsAgentException
import raven.SendSmsParams
import raven.SmsAgent

internal class KilaKonaSmsAgentImpl(private val options: KilaKonaOptions) : SmsAgent {
    override fun credit(): Later<Int> = options.scope.later {
        val json = options.http.get(options.endpoint.balance) { headers(options) }.bodyAsText()

        val data = options.codec.decodeFromString(JsonObject.serializer(), json).ensureSuccess()

        data["totalSms"]?.jsonPrimitive?.intOrNull ?: throw KilaKonaSmsAgentException(
            message = "Couldn't retrieve data.totalSms information even though the data was found"
        )
    }

    override fun send(params: SendSmsParams): Later<SendSmsParams> = options.scope.later {
        var credit = credit().await()
        val warning = options.warning
        if (credit > warning.to.size && credit <= options.warning.limit && warning.to.isNotEmpty()) {
            val p = SendSmsParams(
                from = params.from,
                to = options.warning.to,
                body = options.warning.message(credit)
            )
            execute(p).await()
            credit--
        }

        if (credit < params.to.size) {
            throw KilaKonaSmsAgentException("Running low on credit, can't send ${params.to.size} messages with a $credit credit")
        }
        execute(params).await()
    }

    private fun execute(params: SendSmsParams): Later<SendSmsParams> = options.scope.later {
        val serializer = JsonObject.serializer()
        val json = options.http.post(options.endpoint.sms) {
            headers(options)
            setBody(options.codec.encodeToString(serializer, params.toJsonObject()))
        }.bodyAsText()
        options.codec.decodeFromString(serializer, json).ensureSuccess()
        options.outbox?.store(params)?.await() ?: params
    }

    override fun canSend(count: Int) = credit().then { it > count }.catch { false }
}
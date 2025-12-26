package raven.internal

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import raven.KilaKonaOptions
import raven.KilaKonaSmsAgentException
import raven.SendSmsParams
import raven.SmsAgent

internal class KilaKonaSmsAgentImpl(private val options: KilaKonaOptions) : SmsAgent {
    override suspend fun credit(): Int {
        val json = options.http.get(options.endpoint.balance) { headers(options) }.bodyAsText()

        val data = options.codec.decodeFromString(JsonObject.serializer(), json).ensureSuccess()

        return data["totalSms"]?.jsonPrimitive?.intOrNull ?: throw KilaKonaSmsAgentException(
            message = "Couldn't retrieve data.totalSms information even though the data was found"
        )
    }

    override suspend fun send(params: SendSmsParams): SendSmsParams {
        var credit = credit()
        val warning = options.warning
        if (credit > warning.to.size && credit <= options.warning.limit && warning.to.isNotEmpty()) {
            val p = SendSmsParams(
                from = params.from,
                to = options.warning.to,
                body = options.warning.message(credit)
            )
            execute(p)
            credit--
        }

        if (credit < params.to.size) {
            throw KilaKonaSmsAgentException("Running low on credit, can't send ${params.to.size} messages with a $credit credit")
        }
        return execute(params)
    }

    private suspend fun execute(params: SendSmsParams): SendSmsParams {
        val serializer = JsonObject.serializer()
        val json = options.http.post(options.endpoint.sms) {
            headers(options)
            setBody(options.codec.encodeToString(serializer, params.toJsonObject()))
        }.bodyAsText()
        options.codec.decodeFromString(serializer, json).ensureSuccess()
        return options.outbox?.store(params) ?: params
    }

    override suspend fun canSend(count: Int) = credit() >= count
}
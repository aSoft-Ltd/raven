package raven.internal

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.encodeURLPath
import io.ktor.util.encodeBase64
import koncurrent.Later
import koncurrent.later
import koncurrent.later.await
import koncurrent.awaited.catch
import koncurrent.awaited.then
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import raven.SendSmsParams
import raven.SmsAgent
import raven.TelkoshOptions
import raven.TelkoshSmsAgentException

internal class TelkoshSmsAgentImpl(private val options: TelkoshOptions) : SmsAgent {
    private fun Map<String, Any>.asQuery() = entries.joinToString("&") { (key, value) -> "$key=$value" }
    private fun Map<String, Any>.asJson() = entries.joinToString(prefix = "{\n", separator = ",\n", postfix = "\n}") { (key, value) ->
        """  "$key": "$value""""
    }

    private fun TelkoshOptions.toParams() = mapOf(
        "user" to user,
        "pwd" to password
    )

    override fun credit(): Later<Int> = options.scope.later {
        val url = options.endpoint.balance + "?" + options.toParams().asQuery()
        println(url)
        return@later 100
        val json = options.http.get(url) {
            headers()
        }.bodyAsText()

        println(json)
        val data = options.codec.decodeFromString(JsonObject.serializer(), json).ensureSuccess()

        data["totalSms"]?.jsonPrimitive?.intOrNull ?: throw TelkoshSmsAgentException(
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
            throw TelkoshSmsAgentException("Running low on credit, can't send ${params.to.size} messages with a $credit credit")
        }
        execute(params).await()
    }

    private fun SendSmsParams.toMap() = options.toParams() + mapOf(
        "sender" to from,
        "number" to to.joinToString(","),
//        "msg" to """"$body"""",
        "msg" to body,
//        "smstype" to 0
    )

    private fun execute(params: SendSmsParams): Later<SendSmsParams> = options.scope.later {
        val serializer = JsonObject.serializer()
//        val url = options.endpoint.message + "?" + params.toMap().asQuery().encodeURLPath()
//        val url = options.endpoint.message + "?" + params.toMap().asQuery()
        val url = options.endpoint.message
        println(url)
        val req = "[${params.toMap().asJson()}]"
//        println(req)
        val json = options.http.post(url) {
            headers()
            setBody(req)
        }.bodyAsText()

//        val json = options.http.get(url) {
//            headers()
//        }.bodyAsText()

        println("==================")
        println(json)
        println("==================")
        options.codec.decodeFromString(serializer, json).ensureSuccess()
        options.outbox?.store(params)?.await() ?: params
    }

    override fun canSend(count: Int) = credit().then { it > count }.catch { false }
}
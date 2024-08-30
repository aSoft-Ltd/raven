package raven.internal

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import koncurrent.Later
import koncurrent.later
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import raven.AccountService
import raven.BrevoOptions
import raven.BrevoServiceException

internal class BrevoAccountService(private val options: BrevoOptions) : AccountService {
    override fun credit(): Later<Int> = options.scope.later {
        val json = options.http.get(options.endpoint.account) {
            headers(options)
        }.bodyAsText()

        val resp = options.codec.decodeFromString(JsonObject.serializer(), json)

        val plans = resp["plan"]?.jsonArray ?: throw BrevoServiceException("Couldn't fetch account information")

        val email = plans.filterIsInstance<JsonObject>().filterNot {
            it["type"]?.jsonPrimitive?.content == "sms"
        }.firstOrNull() ?: throw BrevoServiceException(
            message = "Couldn't obtain plan information, there is a change you moved from a free plan to a paid plan just check with Brevo"
        )

        email["credits"]?.jsonPrimitive?.intOrNull ?: throw BrevoServiceException(
            message = "Could not get credit information even though ${email["type"]?.jsonPrimitive?.content} plan was deduced"
        )
    }
}
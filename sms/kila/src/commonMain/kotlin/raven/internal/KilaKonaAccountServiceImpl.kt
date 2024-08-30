package raven.internal

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.headers
import koncurrent.Later
import koncurrent.TODOLater
import koncurrent.later
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import raven.AccountService
import raven.KilaKonaOptions
import raven.KilaKonaSmsServiceException

internal class KilaKonaAccountServiceImpl(private val options: KilaKonaOptions) : AccountService {
    override fun credit(): Later<Int> = options.scope.later {
        val json = options.http.get(options.endpoint.balance) { headers(options) }.bodyAsText()

        val data = options.codec.decodeFromString(JsonObject.serializer(), json).ensureSuccess()

        data["totalSms"]?.jsonPrimitive?.intOrNull ?: throw KilaKonaSmsServiceException(
            message = "Couldn't retrieve data.totalSms information even though the data was found"
        )
    }
}
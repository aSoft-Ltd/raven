package raven

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import raven.MessageConfiguration
import raven.SendSmsParams
import raven.toConsoleEmailAgent
import raven.toConsoleSmsAgent
import raven.toLocalEmailOutbox
import raven.toLocalSmsOutbox

@Serializable
class TestAppConfiguration(
    val sms: MessageConfiguration<SendSmsParams>,
    val email: MessageConfiguration<SendEmailParams>
) {

    private val http by lazy { HttpClient { } }
    private val scope by lazy { CoroutineScope(SupervisorJob()) }
    private val codec by lazy { Json { ignoreUnknownKeys = true } }


    init {
        sms.register {
            outbox("local") { it.toLocalSmsOutbox() }
            agent("console") { it.toConsoleSmsAgent() }
            agent("kilakona") { it.toKilaKonaSmsAgent(http, codec, scope, warning = { count -> "You have $count messages left. Please top up" }) }
        }

        email.register {
            outbox("local") { it.toLocalEmailOutbox() }
            agent("console") { it.toConsoleEmailAgent() }
        }
    }

    private val service by lazy { TestAppService(toOptions()) }

    private val controller by lazy {
        TestAppController(
            service,
            codec = codec
        )
    }

    fun toOptions() = TestAppOptions(this)

    fun toController(): TestAppController = controller

    fun toService(): TestAppService = service
}
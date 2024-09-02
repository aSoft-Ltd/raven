package raven

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import raven.MessageConfiguration
import raven.SendEmailParams
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

    init {
        sms.register {
            outbox("local") { it.toLocalSmsOutbox() }
            agent("console") { it.toConsoleSmsAgent() }
        }

        email.register {
            outbox("local") { it.toLocalEmailOutbox() }
            agent("console") { it.toConsoleEmailAgent() }
        }
    }

    private val service by lazy { TestAppService(toOptions()) }

    private val codec by lazy { Json { ignoreUnknownKeys = true } }

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
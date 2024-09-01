import io.ktor.client.HttpClient
import kommander.expect
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import net.peanuuutz.tomlkt.Toml
import raven.MessageConfiguration
import raven.Outbox
import raven.SendEmailParams
import raven.SendSmsParams
import raven.toConsoleSmsAgent
import raven.toKilaKonaSmsAgent
import raven.toLocalSmsOutbox
import raven.toService

class DynamicSmsConfigurationTest {

    @Serializable
    class ApplicationConfiguration(
        val name: String,
        val sms: MessageConfiguration
    )

    private val codec: StringFormat = Toml {
        ignoreUnknownKeys = true
    }

    @Test
    fun should_be_able_to_configure_different_email_agents_and_services() = runTest {
        val text = """   
            name = "Test" 
                      
            [[sms.outbox]]
            type = "local"
            capacity = 10
            
            [[sms.agent]]
            type = "kilakona"
            key = "<kila-kona-api-key>"
            secret = "<kila-kona-secret>"
            warning_recipients = "andylamax@programmer.net"
            warning_limit = 1000
            
            [[sms.agent]]
            type = "console"
            outbox = "local"
        """.trimIndent()

        val config = codec.decodeFromString(ApplicationConfiguration.serializer(), text)
        expect(config.name).toBe("Test")

        val http = HttpClient {}
        val scope = CoroutineScope(SupervisorJob())
        val codec = Json {}

        var outbox: Outbox<SendSmsParams>? = null
        val service = config.sms.register {
            outbox("local") { params ->
                params.toLocalSmsOutbox().also { outbox = it }
            }
            agent("console") { it.toConsoleSmsAgent() }
            agent("kilakona") {
                it.toKilaKonaSmsAgent(http, codec, scope, warning = { count -> "Your you only have $count emails left, please top up" })
            }
        }.toService()

        expect(service).toBeNull()
        expect(outbox).toBeNull()
    }
}
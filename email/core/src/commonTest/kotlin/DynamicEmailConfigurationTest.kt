import io.ktor.client.HttpClient
import kommander.expect
import koncurrent.later.await
import kotlin.math.exp
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
import raven.toBrevoAgent
import raven.toConsoleEmailAgent
import raven.toLocalEmailOutbox
import raven.toService

class DynamicEmailConfigurationTest {

    @Serializable
    class ApplicationConfiguration(
        val name: String,
        val email: MessageConfiguration
    )

    private val codec: StringFormat = Toml {
        ignoreUnknownKeys = true
    }

    @Test
    fun should_be_able_to_configure_different_email_agents_and_services() = runTest {
        val text = """   
            name = "Test" 
                      
            [[email.outbox]]
            type = "local"
            capacity = 10
            
            [[email.agent]]
            type = "brevo"
            key = "<test-api-key>"
            warning_recipients = "andylamax@programmer.net"
            warning_limit = 400
            
            [[email.agent]]
            type = "console"
            outbox = "local"
        """.trimIndent()

        val config = codec.decodeFromString(ApplicationConfiguration.serializer(), text)
        expect(config.name).toBe("Test")

        val http = HttpClient {}
        val scope = CoroutineScope(SupervisorJob())
        val codec = Json {}

        var outbox: Outbox<SendEmailParams>? = null
        val service = config.email.register {
            outbox("local") { params -> params.toLocalEmailOutbox().also { outbox = it } }
            agent("console") { it.toConsoleEmailAgent() }
            agent("brevo") {
                it.toBrevoAgent(http, codec, scope, warning = { count -> "Your you only have $count emails left, please top up" })
            }
        }.toService()

        expect(outbox).toBeNull()
        expect(service).toBeNull()
    }
}
import io.ktor.server.testing.testApplication
import kommander.expect
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import koncurrent.later.await
import kotlinx.serialization.json.Json
import raven.ConsoleSmsAgent
import raven.ConsoleSmsAgentOptions
import raven.LocalSmsOutbox
import raven.OutboxController
import raven.OutboxDestination
import raven.RemoteOutbox
import raven.RemoteOutboxOptions
import raven.SendSmsParams
import raven.buildSmsService
import raven.installOutbox

class SmsOutboxServerClientTest {

    private val controller = OutboxController(
        service = LocalSmsOutbox(),
        endpoint = OutboxDestination("/email"),
        codec = Json {}
    )

    private val scope = CoroutineScope(SupervisorJob())

    private val service = buildSmsService {
        add(ConsoleSmsAgent(ConsoleSmsAgentOptions(controller.service)))
    }

    @Test
    fun should_be_able_to_retrieve_emails_in_the_out_box() = testApplication {
        routing {
            installOutbox(controller)
        }

        val options = RemoteOutboxOptions<SendSmsParams>(
            http = createClient { },
            codec = controller.codec,
            scope = scope,
            endpoint = controller.endpoint
        )
        val outbox = RemoteOutbox(options)

        val message = SendSmsParams(
            from = "515151",
            to = "+255752748674",
            body = "This is a test email to the server"
        )
        service.send(message).await()

        expect(outbox.sent("+255752748674").await()).toBeOfSize(1)
    }

    @Test
    fun should_delete_already_existing_messages() = testApplication {
        routing {
            installOutbox(controller)
        }
        val options = RemoteOutboxOptions<SendSmsParams>(
            http = createClient { },
            codec = controller.codec,
            scope = scope,
            endpoint = controller.endpoint
        )
        val outbox = RemoteOutbox(options)
        expect(outbox.sent("+255752748674").await()).toBeOfSize(0)

        val message = SendSmsParams(
            from = "515151",
            to = "+255752748674",
            body = "This is a test email to the server"
        )
        service.send(message).await()

        expect(outbox.sent("+255752748674").await()).toBeOfSize(1)

        outbox.delete("+255752748674").await()

        expect(outbox.sent("+255752748674").await()).toBeOfSize(0)
    }
}
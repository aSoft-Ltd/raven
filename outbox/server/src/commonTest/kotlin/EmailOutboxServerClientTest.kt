import io.ktor.server.testing.testApplication
import kommander.expect
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import raven.ConsoleEmailAgent
import raven.ConsoleEmailAgentOptions
import raven.LocalEmailOutbox
import raven.OutboxController
import raven.OutboxDestination
import raven.RemoteOutbox
import raven.RemoteOutboxOptions
import raven.SendEmailParams
import raven.buildEmailService
import raven.installOutbox

class EmailOutboxServerClientTest {

    private val controller = OutboxController(
        service = LocalEmailOutbox(),
        endpoint = OutboxDestination("/email"),
        codec = Json {}
    )

    private val scope = CoroutineScope(SupervisorJob())

    private val service = buildEmailService {
        add(ConsoleEmailAgent(ConsoleEmailAgentOptions(controller.service)))
    }

    @Test
    fun should_be_able_to_retrieve_emails_in_the_out_box() = testApplication {
        routing {
            installOutbox(controller)
        }

        val options = RemoteOutboxOptions<SendEmailParams>(
            http = createClient { },
            codec = controller.codec,
            scope = scope,
            endpoint = controller.endpoint
        )
        val outbox = RemoteOutbox(options)

        val message = SendEmailParams(
            from = "sender@test.com",
            to = "receiver@test.com",
            subject = "Test Email",
            body = "This is a test email to the server"
        )
        service.send(message).await()

        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(1)
    }

    @Test
    fun should_delete_already_existing_messages() = testApplication {
        routing {
            installOutbox(controller)
        }
        val options = RemoteOutboxOptions<SendEmailParams>(
            http = createClient { },
            codec = controller.codec,
            scope = scope,
            endpoint = controller.endpoint
        )
        val outbox = RemoteOutbox(options)
        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(0)

        val message = SendEmailParams(
            from = "sender@test.com",
            to = "receiver@test.com",
            subject = "Test Email",
            body = "This is a test email to the server"
        )
        service.send(message).await()

        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(1)

        outbox.delete("receiver@test.com").await()

        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(0)
    }
}
import io.ktor.server.testing.testApplication
import kommander.expect
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.future.await
import kotlinx.serialization.json.Json
import raven.ConsoleEmailAgent
import raven.ConsoleEmailAgentOptions
import raven.LocalEmailOutbox
import raven.OutboxController
import raven.OutboxDestination
import raven.RemoteEmailOutbox
import raven.RemoteEmailOutboxOptions
import raven.SendEmailParams
import raven.buildEmailService
import raven.installOutbox

class ServerClientTest {

    val controller = OutboxController(
        service = LocalEmailOutbox(),
        endpoint = OutboxDestination("/email"),
        codec = Json {}
    )

    val scope = CoroutineScope(SupervisorJob())

    val service = buildEmailService {
        add(ConsoleEmailAgent(ConsoleEmailAgentOptions(controller.service)))
    }

    @Test
    fun should_be_able_to_retrieve_emails_in_the_out_box() = testApplication {
        routing {
            installOutbox(controller)
        }

        val options = RemoteEmailOutboxOptions(
            http = createClient { },
            codec = controller.codec,
            scope = scope,
            endpoint = controller.endpoint
        )
        val outbox = RemoteEmailOutbox(options)

        val email = SendEmailParams(
            from = "sender@test.com",
            to = "receiver@test.com",
            subject = "Test Email",
            body = "This is a test email to the server"
        )
        service.send(email).await()

        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(1)
    }

    @Test
    fun should_delete_already_existing_messages() = testApplication {
        routing {
            installOutbox(controller)
        }
        val options = RemoteEmailOutboxOptions(
            http = createClient { },
            codec = controller.codec,
            scope = scope,
            endpoint = controller.endpoint
        )
        val outbox = RemoteEmailOutbox(options)
        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(0)

        val email = SendEmailParams(
            from = "sender@test.com",
            to = "receiver@test.com",
            subject = "Test Email",
            body = "This is a test email to the server"
        )
        service.send(email).await()

        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(1)

        outbox.delete("receiver@test.com").await()

        expect(outbox.sent("receiver@test.com").await()).toBeOfSize(0)
    }
}
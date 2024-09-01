import kommander.expect
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import raven.ConsoleEmailAgent
import raven.ConsoleEmailAgentOptions
import raven.LocalEmailOutbox
import raven.SendEmailParams
import raven.buildEmailService

class EmailServiceTest {

    private val outbox = LocalEmailOutbox()

    private val service = buildEmailService {
        add(ConsoleEmailAgent(ConsoleEmailAgentOptions()))
    }

    @Test
    fun should_be_able_to_construct_a_service_with_multiple_senders() = runTest {
        val params = SendEmailParams(
            from = "sender@test.com",
            to = "receiver@test.com",
            subject = "Test Email",
            body = "This is a test email"
        )
        service.send(params).await()
        expect(outbox.sent(to = "receiver@test.com").await()).toBeOfSize(1)
    }
}
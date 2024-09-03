import io.ktor.server.testing.testApplication
import kommander.expect
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import net.peanuuutz.tomlkt.Toml
import raven.RemoteOutbox
import raven.RemoteOutboxOptions
import raven.SendEmailParams
import raven.SendSmsParams
import raven.TestAppConfiguration
import raven.installOutbox

class OutboxServerClientTest {

    val config = """
        [[sms.outbox]]
        type = "local"
        
        [[sms.agent]]
        type = "console"
        outbox = "local"
       
        [[email.outbox]]
        type = "local"
        
        [[email.agent]]
        type = "console"
        outbox = "local"
    """.trimIndent()

    private val scope = CoroutineScope(SupervisorJob())

    @Test
    fun should_be_able_to_retrieve_emails_in_the_out_box() = testApplication {
        val configuration = Toml.decodeFromString(TestAppConfiguration.serializer(), config)

        val service = configuration.toService()
        val controller = configuration.toController()

        routing {
            installOutbox(controller.email)
            installOutbox(controller.sms)
        }

//        val email = controller.email
//        if (email != null) run { // email test
//            val options = RemoteOutboxOptions<SendEmailParams>(
//                http = createClient { },
//                codec = email.codec,
//                scope = scope,
//                endpoint = email.endpoint
//            )
//            val outbox = RemoteOutbox(options)
//
//            val message = SendEmailParams(
//                from = "sender@test.com",
//                to = "receiver@test.com",
//                subject = "Test Email",
//                body = "This is a test email to the server"
//            )
//            service.agents.email.send(message).await()
//
//            expect(outbox.sent("receiver@test.com").await()).toBeOfSize(1)
//        }

        val sms = controller.sms
        if (sms != null) run { // email test
            val options = RemoteOutboxOptions<SendSmsParams>(
                http = createClient { },
                codec = sms.codec,
                scope = scope,
                endpoint = sms.endpoint
            )
            val outbox = RemoteOutbox(options)

            val destination = "+255752988988"
//            val destination = "+255752748674"
            val message = SendSmsParams(
                from = "KILAKONA",
                to = destination,
                body = "This is a test sms to Isaka"
            )
            service.agents.sms.send(message).await()

            expect(outbox.sent(destination).await()).toBeOfSize(1)
        }
    }
}
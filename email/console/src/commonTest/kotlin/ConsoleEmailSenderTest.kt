import kommander.expect
import koncurrent.later.await
import kotlinx.coroutines.test.runTest
import raven.MockAttachment
import kotlin.test.Test
import raven.Address
import raven.ConsoleEmailAgent
import raven.ConsoleEmailAgentOptions
import raven.PrettyConsoleEmailFormatter
import raven.SendEmailParams

class ConsoleEmailSenderTest {
    val sender = ConsoleEmailAgent(ConsoleEmailAgentOptions(formatter = PrettyConsoleEmailFormatter()))

    @Test
    fun should_easily_send_an_email() = runTest {
        val params = SendEmailParams(
            subject = "Test Draft",
            from = "from@test.com",
            to = "to@gmail.com",
            body = "This is a test email"
        )
        val message = sender.send(params).await()
        expect(message).toBeNonNull()
    }

    @Test
    fun should_support_attachments() = runTest {
        val params = SendEmailParams(
            from = "from@test.com",
            to = "to@gmail.com",
            subject = "This is a test draft",
            body = "This is a test email",
            attachments = listOf(
                MockAttachment(4, "number", "test attachment"),
                MockAttachment(sender, "mailer", "Attached the freaking mailer son"),
            )
        )
        val message = sender.send(params).await()
        expect(message).toBeNonNull()
    }

    @Test
    fun should_look_good_even_on_the_console() = runTest {
        val params = SendEmailParams(
            from = Address(
                name = "Dope Developer",
                email = "anderson@developer.com"
            ),
            to = Address(
                name = "Console",
                email = "test@console.com"
            ),
            subject = "Look good while doing it",
            body = "When you decide to do something, make sure you do it well and make sure you look good doing it\n" +
                    "It not only makes thr whole thing wow, but even people watching you do enjoy"
        )
        sender.send(params).await()
    }

    @Test
    fun should_print_multiple_recipients_properly() = runTest {
        val params = SendEmailParams(
            from = Address(
                name = "Dope Developer",
                email = "anderson@developer.com"
            ),
            to = listOf(
                Address(
                    name = "Anderson",
                    email = "test@anderson.com"
                ),
                Address(
                    name = "Lugendo",
                    email = "test@luge.com"
                ),
                Address(
                    name = "Vladmir Putin",
                    email = "test@vlad.com"
                ),
            ),
            subject = "Look good while doing it",
            body = "When you decide to do something, make sure you do it well and make sure you look good doing it\n" +
                    "It not only makes thr whole thing wow, but even people watching you do enjoy",
            attachments = listOf(
                MockAttachment(12, "image/jpg", "picture-12.jpg"),
                MockAttachment(13, "image/jpg", "picture-13.jpg"),
                MockAttachment(14, "image/jpg", "picture-14.jpg"),
            )
        )

        sender.send(params).await()
    }
}
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import raven.AbstractEmailServiceTest
import raven.BrevoEmailService
import raven.CreditWarning
import raven.SendEmailParams
import raven.bodyMarkup
import raven.toHtmlString

class BrevoEmailSendingTest : AbstractEmailServiceTest(
    BrevoEmailService(
        apiKey = "<api-key-goes-here>",
        warning = CreditWarning(
            to = "andylamax@programmer.net",
            limit = 290,
            message = { count ->
                bodyMarkup {
                    text("You have $count emails left. Consider topping up before running out of service")
                }.toHtmlString()
            }
        )
    )
) {
    @Test
    fun should_be_able_to_warn_if_credits_are_below_said_values() = runTest {
        val body = bodyMarkup {
            container(center) {
                text(center) { "Hello" }
                text(center.font(weight = "bold")) { "Anderson" }
                text(center) { "This is a test" }
            }
            button(href = "https://asoft.co.tz") {
                text(css.font(size = "30px")) { "Go to aSoft" }
            }
        }

        val params = SendEmailParams(
            from = "developer.academia@gmail.com",
            to = "andylamax@programmer.net",
            subject = "Warning Test",
            body = body.toHtmlString()
        )
        service.sender.send(params).await()
    }
}
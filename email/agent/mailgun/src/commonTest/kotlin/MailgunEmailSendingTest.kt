import kotlin.test.Ignore
import raven.AbstractEmailAgentTest
import raven.MailgunEmailAgent
import raven.CreditWarning
import raven.bodyMarkup
import raven.toHtmlString

@Ignore
class MailgunEmailSendingTest : AbstractEmailAgentTest(
    MailgunEmailAgent(
        key = "<api-key-goes-here>",
        warning = CreditWarning(
            to = listOf("andylamax@programmer.net"),
            limit = 290,
            message = { count ->
                bodyMarkup {
                    text("You have $count emails left. Consider topping up before running out of service")
                }.toHtmlString()
            }
        )
    )
)
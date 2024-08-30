import kotlin.test.Ignore
import raven.AbstractEmailAgentTest
import raven.BrevoEmailAgent
import raven.CreditWarning
import raven.bodyMarkup
import raven.toHtmlString

@Ignore
class BrevoEmailSendingTest : AbstractEmailAgentTest(
    BrevoEmailAgent(
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
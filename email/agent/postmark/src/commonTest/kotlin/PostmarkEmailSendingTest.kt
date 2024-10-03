import raven.AbstractEmailAgentTest
import raven.CreditWarning
import raven.PostmarkEmailAgent
import raven.bodyMarkup
import raven.toHtmlString
import kotlin.test.Ignore

@Ignore
class PostmarkEmailSendingTest : AbstractEmailAgentTest(
    PostmarkEmailAgent(
        token = "<token-goes-here>",
        warning = CreditWarning(
            to = listOf("info@asoft.co.tz"),
            limit = 290,
            message = { count ->
                bodyMarkup {
                    text("You have $count emails left. Consider topping up before running out of service")
                }.toHtmlString()
            }
        )
    )
)
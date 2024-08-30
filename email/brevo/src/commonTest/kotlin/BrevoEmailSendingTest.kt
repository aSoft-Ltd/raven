import kotlin.test.Ignore
import raven.AbstractEmailServiceTest
import raven.BrevoEmailService
import raven.CreditWarning
import raven.bodyMarkup
import raven.toHtmlString

@Ignore
class BrevoEmailSendingTest : AbstractEmailServiceTest(
    BrevoEmailService(
        key = "<api-key-goes-here>",
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
)
import raven.AbstractSmsAgentTest
import raven.TelkoshOptions
import raven.CreditWarning
import raven.TelkoshSmsAgent

//@Ignore
class TelkoshSmsAgentTest : AbstractSmsAgentTest(
    agent = TelkoshSmsAgent(
        options = TelkoshOptions(
            user = "asoft",
            password = "aSoft@1234",
            warning = CreditWarning(
                to = listOf("+255752748674"),
//                to = listOf("+255686448421"),
                limit = 700,
                message = { count ->
                    "You have $count messages left in your telkosh account. Consider topping up"
                }
            )
        )
    ),
    sender = "Academia"
)
import kotlin.test.Ignore
import raven.AbstractSmsAgentTest
import raven.BeemOptions
import raven.BeemSmsAgent
import raven.CreditWarning

@Ignore
class BeamSmsAgentTest : AbstractSmsAgentTest(
    agent = BeemSmsAgent(
        options = BeemOptions(
            warning = CreditWarning(
                to = listOf("+255752748674"),
                limit = 50,
                message = { count ->
                    "You have $count messeges left in your beam account. Consider topping up"
                }
            )
        )
    ),
    sender = "INFO"
)
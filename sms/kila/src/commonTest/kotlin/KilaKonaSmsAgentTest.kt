import raven.AbstractSmsAgentTest
import raven.KilaKonaOptions
import raven.CreditWarning
import raven.KilaKonaSmsAgent

class KilaKonaSmsAgentTest : AbstractSmsAgentTest(
    agent = KilaKonaSmsAgent(
        options = KilaKonaOptions(
            key = "",
            secret = "",
            warning = CreditWarning(
                to = listOf(),
                limit = 700,
                message = { count ->
                    "You have $count messages left in your kilakona account. Consider topping up"
                }
            )
        )
    ),
    sender = "KILAKONA"
)
import raven.AbstractSmsServiceTest
import raven.KilaKonaOptions
import raven.KilaKonaSmsService
import raven.CreditWarning

class KilaKonaSmsServiceTest : AbstractSmsServiceTest(
    service = KilaKonaSmsService(
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
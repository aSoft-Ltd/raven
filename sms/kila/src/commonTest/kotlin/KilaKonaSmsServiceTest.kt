import raven.AbstractSmsServiceTest
import raven.KilaKonaOptions
import raven.KilaKonaSmsService
import raven.CreditWarning

class KilaKonaSmsServiceTest : AbstractSmsServiceTest(
    service = KilaKonaSmsService(
        options = KilaKonaOptions(
            api = "",
            key = "",
            warning = CreditWarning(
                to = "+255752748674",
                limit = 5,
                message = { count ->
                    "You have $count messeges left in your kilakona account. Consider topping up"
                }
            )
        )
    )
)
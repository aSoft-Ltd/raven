import raven.AbstractSmsServiceTest
import raven.BeemOptions
import raven.BeemSmsService
import raven.CreditWarning

class BeamSmsServiceTest : AbstractSmsServiceTest(
    service = BeemSmsService(
        options = BeemOptions(
            warning = CreditWarning(
                to = "+255752748674",
                limit = 50,
                message = { count ->
                    "You have $count messeges left in your beam account. Consider topping up"
                }
            )
        )
    )
)
package raven

import kommander.expect
import kommander.toBeGreaterThan
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

abstract class AbstractSmsServiceTest(
    val service: SmsService
) {
    @Test
    fun should_be_able_to_send_sms() = runTest {
        val params = SendSmsParams(
            from = "Sender",
            to = "+255752748674",
            body = "Hello Anderson, this is a test"
        )
        service.sender.send(params).await()
    }

    @Test
    fun should_be_able_to_get_remaining_credit() = runTest {
        val credit = service.account.credit().await()
        expect(credit).toBeGreaterThan(0)
    }
}
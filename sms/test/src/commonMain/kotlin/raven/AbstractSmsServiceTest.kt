package raven

import kommander.expect
import kommander.toBeGreaterThan
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

abstract class AbstractSmsServiceTest(
    val service: SmsService,
    val sender: String
) {
    @Test
    fun should_be_able_to_send_sms() = runTest {
        val params = SendSmsParams(
            from = sender,
//            to = "+255752748674",
            to = "+255752270749",
            body = "Hello Rachel, congratulations on your schools performance"
        )
        service.sender.send(params).await()
    }

    @Test
    fun should_be_able_to_get_remaining_credit() = runTest {
        val credit = service.account.credit().await()
        expect(credit).toBeGreaterThan(0)
    }
}
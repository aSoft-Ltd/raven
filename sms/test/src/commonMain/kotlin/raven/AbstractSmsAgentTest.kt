package raven

import kommander.expect
import kommander.toBeGreaterThan
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

abstract class AbstractSmsAgentTest(
    val agent: SmsAgent,
    val sender: String
) {
    @Test
    fun should_be_able_to_send_sms() = runTest {
        val params = SendSmsParams(
            from = sender,
            to = "+255752748674",
//            to = "+255752270749",
            body = "Hello Rachel, congratulations on your schools performance"
        )
        agent.send(params)
    }

    @Test
    fun should_be_able_to_get_remaining_credit() = runTest {
        val credit = agent.credit()
        expect(credit).toBeGreaterThan(0)
    }
}
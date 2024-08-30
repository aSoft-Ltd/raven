package raven

import kommander.expect
import kommander.toBeGreaterThan
import koncurrent.later.await
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

abstract class AbstractEmailAgentTest(
    val agent: EmailAgent
) {
    @Test
    fun should_be_able_to_send_email() = runTest {
        val body = bodyMarkup {
            container(center) {
                text(center) { "Hello" }
                text(center.font(weight = "bold")) { "Anderson" }
                text(center) { "This is a test" }
            }
            button(href = "https://asoft.co.tz") {
                text(css.font(size = "30px")) { "Go to aSoft" }
            }
        }
        val params = SendEmailParams(
            from = "developer.academia@gmail.com",
            to = "andylamax@programmer.net",
            subject = "Test Email 5",
            body = body.toHtmlString()
        )
        agent.send(params).await()
    }

    @Test
    fun should_be_able_to_get_remaining_credit() = runTest {
        val credit = agent.credit().await()
        expect(credit).toBeGreaterThan(0)
    }
}
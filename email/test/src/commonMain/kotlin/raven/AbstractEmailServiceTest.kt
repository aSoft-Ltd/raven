package raven

import kommander.expect
import kommander.toBeGreaterThan
import koncurrent.later.await
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

abstract class AbstractEmailServiceTest(
    val service: EmailService
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
        service.sender.send(params).await()
    }

    @Test
    fun should_be_able_to_get_remaining_credit() = runTest {
        val credit = service.account.credit().await()
        println(credit)
        expect(credit).toBeGreaterThan(0)
    }
}
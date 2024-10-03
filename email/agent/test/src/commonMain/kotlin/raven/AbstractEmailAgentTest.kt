@file:OptIn(ExperimentalEncodingApi::class)

package raven

import kommander.expect
import kommander.toBeGreaterThan
import koncurrent.later.await
import kotlinx.coroutines.test.runTest
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.test.Test

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
            img(src = "cid:logo", alt = "logo", style = css.width("500px"))
//            img(src = "https://demo.academia.ne.tz/static/images/onboard.png", alt = "logo", style = css.width("500px"))
        }

        val rand = Random.nextInt(100..999)

        val file = Base64Resource(
            name = "logo-embedded.png",
            type = "image/png",
            id = "logo",
        ) {
            image
        }

//        val file = ByteArrayResource(
//            name = "logo-embedded.png",
//            type = "image/png",
//            id = "logo",
//        ) {
//            Base64.decode(image)
//        }
        val params = SendEmailParams(
            from = "academia@asoft.co.tz",
//            from = "software@asoft.co.tz",
//            to = "andylamax@programmer.net",
//            to = "andylamax@gmail.com",
            to = "info@asoft.co.tz",
            subject = "Test Email $rand",
            inline = listOf(file),
            attachments = listOf(
                file.copy(name = "logo1.png", id = "logo1"),
                file.copy(name = "logo2.png", id = "logo2"),
                file.copy(name = "logo3.png", id = "logo3"),
            ),
            body = body.toHtmlString()
        )
        agent.send(params).await()
    }

    @Test
    fun should_be_able_to_get_remaining_credit() = runTest {
        val credit = agent.credit().await()
        println("Credit: $credit")
        expect(credit).toBeGreaterThan(0)
    }

    //    private val image = """data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="""
    private val image = """iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=="""
}
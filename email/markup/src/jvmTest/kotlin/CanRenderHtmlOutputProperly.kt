import kotlin.test.Test
import raven.Body
import raven.ComponentScope
import raven.Container
import raven.bodyMarkup
import raven.css
import raven.toHtmlFile

class CanRenderHtmlOutputProperly {

    fun ComponentScope<Body>.ReceptionistBarner(
        background: String,
        foreground: String,
        logo: String,
        name: String,
        receptionist: String,
    ) = container(css.background(color = background, image = "linear-gradient(45deg,${background}ff,${background}80,${background}aa)").color(foreground)) {
        container(css.max(width = "50em").position("relative")) {
            row {
                col(css.width("10%")) {
                    img(
                        src = logo,
                        alt = "logo",
                        style = css.max(width = "80px").padding("3em", "0")
                    )
                }
                col(css.width("90%").text(align = "left")) {
                    label(css.font(size = "1.6em"), "Hello, $name")
                }
            }
            img(
                src = receptionist,
                alt = "receptionist",
                style = css.position("absolute").max(width = "50%").right("0").top("max(8%,3em)")
            )
        }
    }

    fun ComponentScope<Body>.ReceptionlessBarner(
        background: String,
        foreground: String,
        logo: String,
        label: String,
    ) = row(css.background(color = background).color(foreground)) {
        col(center.width("100%").padding(v = "1em")) {
            img(
                src = logo,
                alt = "logo",
                style = css.display("inline-block").max(width = "80px")
            )
            br()
            label(css.font(size = "1.6em"), label)
        }
    }


    fun ComponentScope<Container>.VerificationMeatContent(
        brand: String,
        background: String,
        foreground: String,
        link: String,
        token: String,
    ) {
        p { text("We are thrilled to have you join us") }
        p {
            text(
                """
                   To be able to proceed with your registration while keeping your data secure, 
                   we need you to verify your email by clicking the "verify email" button below.
                """.trimIndent()
            )
        }
        container(center.padding(v = "1.5em")) {
            button(css.background(background), href = "$link?token=$token") {
                label(css.color(foreground), "Verify Email")
            }
        }
        p {
            text("Thanks!")
            br()
            text("~ The $brand team")
        }
    }

    fun ComponentScope<Body>.ReceptionistVerificationMeat(
        brand: String,
        label: String,
        background: String,
        foreground: String,
        link: String,
        token: String,
    ) = container(css.max(width = "40em").padding(top = "6em", bottom = "2em", left = "2em", right = "2em")) {
        row {
            col(center) {
                text(css.font(size = "1.5em", weight = "bold"), label)
            }
        }
        VerificationMeatContent(brand, background, foreground, link, token)
    }

    fun ComponentScope<Body>.ReceptionlessVerificationMeat(
        brand: String,
        name: String,
        background: String,
        foreground: String,
        link: String,
        token: String,
    ) = container(css.max(width = "40em").padding(v = "2em", h = "2em")) {
        p { text(css.font(size = "1em", weight = "bold"), "Hello $name,") }
        VerificationMeatContent(brand, background, foreground, link, token)
    }

    fun ComponentScope<Body>.Footer(
        brand: String,
        background: String,
        address: String,
        year: String,
        socials: Map<String, String>
    ) = container(css.background("#f9fafb").color("#6c7f93").padding(top = "2em")) {
        container(center.max(width = "40em").padding(h = "2em")) {
            p {
                text("This email was sent to you because you registered at")
                br()
                text(css.color(background), brand)
            }
            p {
                text("If you did not do register this kind of action, report ")
                text(css.color(background), "here")
            }
            p { text(address) }
            p { text("Copyright ${169.toChar()} $year") }
            row(css.padding(top = "1em").max(width = "20em")) {
                for (s in socials) col(center) {
                    a(href = s.key) {
                        img(style = css.display("inline-block").width("30px"), src = s.value, alt = s.key)
                    }
                }
            }
        }
        row(css.height("3em").margin(top = "3em").background(background)) {}
    }

    fun verification(
        brand: String,
        background: String,
        foreground: String,
        logo: String,
        name: String,
        receptionist: String,
        link: String,
        token: String,
        address: String,
        year: String,
        socials: Map<String, String>
    ) = bodyMarkup(css().font(family = "Inter,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Ubuntu,sans-serif;")) {
        ReceptionlessBarner(background, foreground, logo, brand)
        ReceptionlessVerificationMeat(brand, name, background, foreground, link, token)
//        ReceptionistBarner(background, foreground, logo, name, receptionist)
//        ReceptionistMeat(brand, "Welcome to $brand", background, foreground, link, token)
        Footer(brand, background, address, year, socials)
    }

    fun recovery(
        brand: String,
        background: String,
        foreground: String,
        logo: String,
        name: String,
        receptionist: String,
        link: String,
        token: String,
        address: String,
        year: String,
        socials: Map<String, String>
    ) = bodyMarkup(css().font(family = "Inter,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Ubuntu,sans-serif;")) {
        ReceptionlessBarner(background, foreground, logo, brand)
        ReceptionlessVerificationMeat(brand, name, background, foreground, link, token)

        Footer(brand, background, address, year, socials)
    }

    class Brand(
        val name: String,
        val background: String,
        val foreground: String,
        val logo: String
    )

    @Test
    fun should_be_able_to_render_to_a_html_file() {
        val capital = Brand("PiCapital", "#45d68b", "#efefef", "templates/images/logo2.png")
        val kipesa = Brand("Kipesa", "#479155", "#efefef", "templates/images/logo.png")
        val quisine = Brand("Quisine de l'afrique", "#ba3a0b", "#efefef", "https://logo.com/image-cdn/images/kts928pd/production/b374a124fc505ab3255fadae257d90e8e4a4855e-449x432.png?w=1080&q=72")
//        val brand = capital
//        val brand = kipesa
        val brand = quisine
        val email1 = verification(
            brand = brand.name,
            background = brand.background,
            foreground = brand.foreground,
            logo = brand.logo,
            receptionist = "templates/images/receptionist.png",
            name = "Anderson",
            link = "http://test.com",
            token = "tokomeza",
            address = "Whitehouse Asgard Hq, Asgard",
            year = "2023",
            socials = mapOf(
                "https://linkedin.com/company/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/linkedin_sbsrmq.png",
                "https://facebook.com/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/facebook_dbzmo1.png",
                "https://twitter.com/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/twitter_txahhu.png",
                "https://instagram.com/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/instagram_az4bp4.png"
            )
        )
        email1.toHtmlFile("src/jvmTest/resources/verification.html", "  ")

        val email2 = recovery(
            brand = brand.name,
            background = brand.background,
            foreground = brand.foreground,
            logo = brand.logo,
            receptionist = "templates/images/receptionist.png",
            name = "Anderson",
            link = "http://test.com",
            token = "tokomeza",
            address = "Whitehouse Asgard Hq, Asgard",
            year = "2023",
            socials = mapOf(
                "https://linkedin.com/company/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/linkedin_sbsrmq.png",
                "https://facebook.com/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/facebook_dbzmo1.png",
                "https://twitter.com/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/twitter_txahhu.png",
                "https://instagram.com/picortex" to "https://res.cloudinary.com/dc3mzhqp1/image/upload/v1689072836/Emails/piemail/instagram_az4bp4.png"
            )
        )
        email2.toHtmlFile("src/jvmTest/resources/recovery.html", "  ")
    }
}
package raven

import raven.exceptions.notBuiltYet

class MarkupFactory {
    internal var root: Html? = null
    fun html(lang: String = "en", dir: String = "ltr", builder: HtmlScope.() -> Unit): Html {
        val scope = HtmlScope()
        scope.builder()
        return Html(
            lang = lang,
            dir = dir,
            head = scope.head,
            body = scope.body
        ).also { root = it }
    }
}

fun markup(builder: MarkupFactory.() -> Unit): EmailMarkup {
    val factory = MarkupFactory().apply(builder)
    return EmailMarkup(factory.root ?: notBuiltYet("html"))
}

fun bodyMarkup(style: Styles = css(), builder: ComponentScope<Body>.() -> Unit) = markup {
    html {
        head {
            contentType()
            viewport()
        }

        body(style) { builder() }
    }
}
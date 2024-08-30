package raven.renderes

import raven.Html

internal fun Html.toHtmlString(tab: String) = buildString {
    appendLine("<html lang=$lang dir=$dir>")
    appendLine(head.toHtmlString(tab))
    appendLine(body.toHtmlString(tab))
    append("</html>")
}
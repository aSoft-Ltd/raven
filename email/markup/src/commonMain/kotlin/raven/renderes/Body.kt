package raven.renderes

import raven.Body

internal fun Body.toHtmlString(tab: String) = buildString {
    appendLine("<body${styles.css}>")
    children.forEach { appendLine(it.toHtmlString(tab,1)) }
    append("</body>")
}
package raven.renderes

import raven.Head

internal fun Head.toHtmlString(tab: String) = buildString {
    appendLine("<head>")
    meta.forEach { m ->
        val props = m.props.joinToString(" ") { """${it.first}="${it.second}"""" }
        appendLine("$tab<meta $props/>")
    }
    append("</head>")
}
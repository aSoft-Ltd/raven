package raven.renderes

import raven.Head

internal fun Head.toHtmlString(tab: String) = buildString {
    appendLine("<head>")
    for(m in meta) appendLine(tag = "meta", props = m.props, tab)
    for(l in link) appendLine(tag = "link", props = l.props,tab)
    append("</head>")
}

private fun StringBuilder.appendLine(tag: String, props: List<Pair<String, String>>, tab: String) {
    val p = props.joinToString(" ") { """${it.first}="${it.second}"""" }
    appendLine("$tab<$tag $p/>")
}
package raven.renderes

import raven.Body
import raven.Break
import raven.Button
import raven.Column
import raven.Component
import raven.Container
import raven.FertileComponent
import raven.Image
import raven.A
import raven.Paragraph
import raven.Row
import raven.SterileComponent
import raven.Text

internal fun Component.toHtmlString(tab: String, level: Int) = when (this) {
    is SterileComponent -> toHtmlString(tab, level)
    is FertileComponent -> toHtmlString(tab, level)
}

private fun FertileComponent.toHtmlString(tab: String, level: Int): String = when (this) {
    is Body -> toHtmlString(tab)
    is Paragraph -> toHtmlString(tab, level)
    is Container -> toHtmlString(tab, level)
    is Row -> toHtmlString(tab, level)
    is Column -> toHtmlString(tab, level)
    is A -> toHtmlString(tab, level)
    is Button -> toHtmlString(tab, level)
}

private fun Button.toHtmlString(tab: String, level: Int) = buildString {
    val pad = tab.repeat(level)
    appendLine("$pad<a${styles.css}${props.inline}>")
    children.forEach { appendLine(it.toHtmlString(tab, level + 1)) }
    append("$pad</a>")
}

private fun A.toHtmlString(tab: String, level: Int) = buildString {
    val pad = tab.repeat(level)
    appendLine("$pad<a${styles.css}${props.inline}>")
    children.forEach { appendLine(it.toHtmlString(tab, level + 1)) }
    append("$pad</a>")
}

private fun Column.toHtmlString(tab: String, level: Int) = buildString {
    val pad0 = tab.repeat(level + 0)
    appendLine("$pad0<td${styles.css}${props.inline}>")
    children.forEach {
        appendLine(it.toHtmlString(tab, level + 1))
    }
    append("$pad0</td>")
}

private fun Row.toHtmlString(tab: String, level: Int) = buildString {
    val pad0 = tab.repeat(level + 0)
    val pad1 = tab.repeat(level + 1)
    val pad2 = tab.repeat(level + 2)
    appendLine("$pad0<table${styles.css}${props.inline}>")
    appendLine("$pad1<tbody>")
    appendLine("$pad2<tr style='width:100%'>")
    children.forEach {
        appendLine(it.toHtmlString(tab, level + 3))
    }
    appendLine("$pad2</tr>")
    appendLine("$pad1</tbody>")
    append("$pad0</table>")
}

private fun Container.toHtmlString(tab: String, level: Int) = buildString {
    val pad0 = tab.repeat(level + 0)
    val pad1 = tab.repeat(level + 1)
    val pad2 = tab.repeat(level + 2)
    val pad3 = tab.repeat(level + 3)
    appendLine("$pad0<table${styles.css}${props.inline}>")
    appendLine("$pad1<tbody>")
    appendLine("$pad2<tr style='width:100%'>")
    appendLine("$pad3<td>")
    children.forEach {
        appendLine(it.toHtmlString(tab, level + 4))
    }
    appendLine("$pad3</td>")
    appendLine("$pad2</tr>")
    appendLine("$pad1</tbody>")
    append("$pad0</table>")
}

private fun Paragraph.toHtmlString(tab: String, level: Int): String = buildString {
    val pad = tab.repeat(level)
    appendLine("$pad<p${styles.css}>")
    children.forEach {
        appendLine(it.toHtmlString(tab, level + 1))
    }
    append("$pad</p>")
}

private fun SterileComponent.toHtmlString(tab: String, level: Int) = when (this) {
    is Text -> toHtmlString(tab, level)
    is Image -> toHtmlString(tab, level)
    is Break -> toHtmlString(tab, level)
}

private fun Image.toHtmlString(tab: String, level: Int) = buildString {
    val pad1 = tab.repeat(level)
    append("$pad1<img${styles.css}${props.inline} />")
}

private fun Text.toHtmlString(tab: String, level: Int) = buildString {
    val pad1 = tab.repeat(level)
    val pad2 = tab.repeat(level + 1)
    appendLine("$pad1<span${styles.css}>")
    content.split("\n").forEach { appendLine("$pad2$it") }
    append("$pad1</span>")
}

private fun Break.toHtmlString(tab: String, level: Int) = buildString {
    val pad = tab.repeat(level)
    append("$pad<br />")
}

internal val Map<String, String>.css: String
    get() = if (isEmpty()) "" else {
        " style='${entries.joinToString(";") { "${it.key}:${it.value}" }}'"
    }

internal val Map<String, String>.inline: String
    get() = if (isEmpty()) "" else {
        " ${entries.joinToString(" ") { "${it.key}='${it.value}'" }}"
    }
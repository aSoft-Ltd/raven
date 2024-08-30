package raven

import java.io.File

fun EmailMarkup.toHtmlFile(path: String, tab: String = "  "): File {
    val file = File(path)
    file.createNewFile()
    file.writeText(toHtmlString(tab))
    return file
}
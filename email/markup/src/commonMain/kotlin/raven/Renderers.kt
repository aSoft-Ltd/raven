package raven

import raven.renderes.toHtmlString

fun EmailMarkup.toHtmlString(tab: String = " ") = buildString{
    appendLine("""<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">""")
    append(html.toHtmlString(tab))
}
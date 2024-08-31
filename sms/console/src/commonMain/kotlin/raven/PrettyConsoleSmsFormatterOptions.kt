@file:JsExport
package raven

import kotlinx.JsExport

class PrettyConsoleSmsFormatterOptions(
    val separator: String = "=",
    /**
     * It looks better when this value is an odd number
     */
    val width: Int = 95,
    val margin: Int = 24,
    val border: String = "|",
    val padding: Int = 1,
)
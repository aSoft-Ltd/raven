package raven

import kotlin.math.max

class PrettyConsoleSmsFormatter(private val options: PrettyConsoleSmsFormatterOptions = PrettyConsoleSmsFormatterOptions()) : ConsoleSmsFormatter {

    private val margin = " ".repeat(options.margin)
    private val padding = " ".repeat(options.padding)
    override fun format(params: SendSmsParams): String {
        val separator = "${options.separator} ".repeat(3) + options.separator
        val output = buildString {
            appendLine(separator)
            appendLine("SMS Inbox")
            appendLine(separator)
            appendLine("From:        ${params.from}")
            appendRecipients(params.to)
            appendLine(separator)
            appendMultiLines(params.body)
            append(separator)
        }
        val outLines = output.split("\n")
        val width = max(outLines.maxOf { it.length }, options.width)
        val adjustedLines = outLines.joinToString(separator = "\n") {
            "${margin}${options.border}${padding}" +
                    if (it == separator) {
                        if (width.mod(2) == 0) {
                            val multiples = width / 2
                            "${options.separator} ".repeat(multiples - 1) + " ${options.separator}"
                        } else {
                            val multiples = (width - 1) / 2
                            "${options.separator} ".repeat(multiples) + options.separator
                        }

                    } else {
                        "$it${" ".repeat(width - it.length)}"
                    } +
                    "${padding}${options.border}"
        }
        return adjustedLines
    }

    private fun StringBuilder.appendRecipients(recipients: List<String>) {
        if (recipients.size == 1) {
            val address = recipients.first()
            appendLine("To:          $address")
        } else {
            val first = recipients.first()
            val rest = recipients - first
            appendLine("To:          $first")
            for (recipient in rest) {
                appendLine("             $recipient")
            }
        }
    }

    private fun StringBuilder.appendMultiLines(body: String) = body.split("\n").flatMap {
        it.chunked(options.width)
    }.forEach { appendLine(it) }
}
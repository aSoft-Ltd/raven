package raven

import kotlin.math.max

class PrettyConsoleEmailFormatter(private val options: PrettyConsoleEmailFormatterOptions = PrettyConsoleEmailFormatterOptions()) : ConsoleEmailFormatter {

    private val margin = " ".repeat(options.margin)
    private val padding = " ".repeat(options.padding)
    override fun format(params: SendEmailParams): String {
        val separator = "${options.separator} ".repeat(3) + options.separator
        val output = buildString {
            appendLine(separator)
            appendLine("Email Inbox")
            appendLine(separator)
            appendLine("Subject:     ${params.subject}")
            appendLine("From:        ${params.from.toDetailsString()}")
            appendRecipients(params.to)
            appendLine(separator)
            appendLine("Attachments: ${params.attachments.joinToString(",") { it.name }}")
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

    private fun StringBuilder.appendRecipients(recipients: List<Address>) {
        if (recipients.size == 1) {
            val address = recipients.first().toDetailsString()
            appendLine("To:          $address")
        } else {
            val first = recipients.first()
            val rest = recipients - first
            appendLine("To:          ${first.toDetailsString()}")
            for (recipient in rest) {
                appendLine("             ${recipient.toDetailsString()}")
            }
        }
    }

    private fun Address.toDetailsString() = if (name == null) email else "$name <${email}>"
    private fun StringBuilder.appendMultiLines(body: String) = body.split("\n").flatMap {
        it.chunked(options.width)
    }.forEach { appendLine(it) }
}
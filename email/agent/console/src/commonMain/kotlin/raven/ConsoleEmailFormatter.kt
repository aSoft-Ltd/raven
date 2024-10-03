package raven

interface ConsoleEmailFormatter {
    fun format(params: SendEmailParams) : String
}
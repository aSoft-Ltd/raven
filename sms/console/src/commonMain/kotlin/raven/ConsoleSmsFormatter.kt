package raven

interface ConsoleSmsFormatter {
    fun format(params: SendSmsParams): String
}
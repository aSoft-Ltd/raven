package raven.internal

import raven.BeemOptions
import raven.BeemSmsServiceException
import raven.SendSmsParams
import raven.SmsAgent

internal class BeemSmsAgentImpl(private val options: BeemOptions) : SmsAgent {
    override suspend fun send(params: SendSmsParams): SendSmsParams {
        var credit = credit()
        val warning = options.warning
        if (credit > warning.to.size && credit <= options.warning.limit && warning.to.isNotEmpty()) {
            val p = SendSmsParams(
                from = params.from,
                to = options.warning.to,
                body = options.warning.message(credit)
            )
            execute(p)
            credit--
        }

        if (credit < params.to.size) {
            throw BeemSmsServiceException("Running low on credit, can't send ${params.to.size} messages with a $credit credit")
        }
        return execute(params)
    }

    override suspend fun credit(): Int = TODO()

    private suspend fun execute(params: SendSmsParams): SendSmsParams = TODO()

    override suspend fun canSend(count: Int) = credit() >= count
}
package raven.internal

import koncurrent.Later
import koncurrent.TODOLater
import koncurrent.later
import koncurrent.later.await
import koncurrent.later.catch
import koncurrent.later.then
import raven.BeemOptions
import raven.BeemSmsServiceException
import raven.SendSmsParams
import raven.SmsSender

internal class BeemSmsSenderImpl(
    private val options: BeemOptions,
    private val service: BeemSmsServiceImpl
) : SmsSender {
    override fun send(params: SendSmsParams): Later<SendSmsParams> = options.scope.later {
        var credit = service.account.credit().await()
        val warning = options.warning
        if (credit > warning.to.size && credit <= options.warning.limit && warning.to.isNotEmpty()) {
            val p = SendSmsParams(
                from = params.from,
                to = options.warning.to,
                body = options.warning.message(credit)
            )
            execute(p).await()
            credit--
        }

        if (credit < params.to.size) {
            throw BeemSmsServiceException("Running low on credit, can't send ${params.to.size} messages with a $credit credit")
        }
        execute(params).await()
    }

    private fun execute(params: SendSmsParams): Later<SendSmsParams> = TODOLater()

    override fun canSend(count: Int) = service.account.credit().then { it > count }.catch { false }
}
package raven

import raven.internal.BeemSmsAgentImpl

fun BeemSmsAgent(
    options: BeemOptions
): SmsAgent = BeemSmsAgentImpl(options)
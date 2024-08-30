package raven

import raven.internal.BeemSmsServiceImpl

fun BeemSmsService(
    options: BeemOptions
): SmsService = BeemSmsServiceImpl(options)
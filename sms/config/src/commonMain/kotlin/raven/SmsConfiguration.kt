package raven

fun MessageConfigurationRegistration<SendSmsParams>.toService() = SmsService(agents)

fun Map<String, String>.toLocalSmsOutbox(): Outbox<SendSmsParams> = toOutbox(isSent = isSmsSent)
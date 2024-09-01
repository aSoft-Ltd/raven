package raven

fun MessageConfigurationRegistration<SendSmsParams>.toService() = SmsService(buildAgents())

fun Map<String, String>.toLocalSmsOutbox(): Outbox<SendSmsParams> = toOutbox(isSent = isSent)
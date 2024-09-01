package raven

fun MessageConfigurationRegistration<SendEmailParams>.toService() = EmailService(buildAgents())

fun Map<String, String>.toLocalEmailOutbox(): Outbox<SendEmailParams> = toOutbox(isSent)
package raven

fun MessageConfigurationRegistration<SendEmailParams>.toService() = EmailService(agents)

fun Map<String, String>.toLocalEmailOutbox(): Outbox<SendEmailParams> = toOutbox(isEmailSent)
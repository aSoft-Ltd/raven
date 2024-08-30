package raven

data class SendEmailTemplateParams(
    val subject: String,
    val from: Address,
    val to: List<Address>,
    val cc: List<Address>,
    val bcc: List<Address>,
    val body: EmailTemplate,
    val attachments: List<EmailAttachment<Any?>> = emptyList(),
)
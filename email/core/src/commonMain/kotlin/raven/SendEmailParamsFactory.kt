package raven

fun SendEmailParams(
    from: String,
    to: String,
    subject: String,
    body: String,
    attachments: List<EmailAttachment<Any>> = emptyList()
) = SendEmailParams(
    from = Address(from),
    to = listOf(Address(to)),
    subject = subject,
    body = body,
    cc = emptyList(),
    bcc = emptyList(),
    attachments = attachments
)

fun SendEmailParams(
    from: Address,
    to: Address,
    subject: String,
    body: String,
    attachments: List<EmailAttachment<Any>> = emptyList()
) = SendEmailParams(
    from = from,
    to = listOf(to),
    subject = subject,
    body = body,
    cc = emptyList(),
    bcc = emptyList(),
    attachments = attachments
)

fun SendEmailParams(
    from: Address,
    to: List<Address>,
    subject: String,
    body: String,
    attachments: List<EmailAttachment<Any>> = emptyList()
) = SendEmailParams(
    from = from,
    to = to,
    subject = subject,
    body = body,
    cc = emptyList(),
    bcc = emptyList(),
    attachments = attachments
)
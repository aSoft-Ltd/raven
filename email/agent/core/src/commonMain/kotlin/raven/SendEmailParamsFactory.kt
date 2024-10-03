package raven

fun SendEmailParams(
    from: String,
    to: String,
    subject: String,
    body: String,
    inline: List<EmbeddedResource> = emptyList(),
    attachments: List<EmbeddedResource> = emptyList()
) = SendEmailParams(
    from = Address(from),
    to = listOf(Address(to)),
    subject = subject,
    body = body,
    cc = emptyList(),
    bcc = emptyList(),
    inline = inline,
    attachments = attachments
)

fun SendEmailParams(
    from: Address,
    to: Address,
    subject: String,
    body: String,
    resources: List<EmbeddedResource> = emptyList(),
    attachments: List<EmbeddedResource> = emptyList()
) = SendEmailParams(
    from = from,
    to = listOf(to),
    subject = subject,
    body = body,
    cc = emptyList(),
    bcc = emptyList(),
    inline = resources,
    attachments = attachments
)

fun SendEmailParams(
    from: Address,
    to: List<Address>,
    subject: String,
    body: String,
    resources: List<EmbeddedResource> = emptyList(),
    attachments: List<EmbeddedResource> = emptyList()
) = SendEmailParams(
    from = from,
    to = to,
    subject = subject,
    body = body,
    cc = emptyList(),
    bcc = emptyList(),
    inline = resources,
    attachments = attachments
)
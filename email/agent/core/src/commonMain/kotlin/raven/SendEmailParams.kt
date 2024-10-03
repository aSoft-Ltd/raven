package raven

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SendEmailParams(
    val subject: String,
    val from: Address,
    val to: List<Address>,
    val cc: List<Address>,
    val bcc: List<Address>,
    val body: String,
    val type: EmailContentType = EmailContentType.html,
    @Transient
    val inline: List<EmbeddedResource> = emptyList(),
    @Transient
    val attachments: List<EmbeddedResource> = emptyList()
)
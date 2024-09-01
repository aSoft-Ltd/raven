package raven

import kotlinx.serialization.Serializable

@Serializable
class MessageConfiguration(
    val outbox: List<Map<String, String>>,
    val agent: List<Map<String, String>>
) {
    fun <P> register(block: MessageConfigurationRegistration<P>.() -> Unit) = MessageConfigurationRegistration<P>(this).apply(block)
}
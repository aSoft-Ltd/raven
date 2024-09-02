package raven

import kotlinx.serialization.Serializable

@Serializable
class MessageConfiguration<P>(
    val outbox: List<Map<String, String>>,
    val agent: List<Map<String, String>>
) {
    private val registration by lazy { MessageConfigurationRegistration<P>(this) }
    fun register(block: MessageConfigurationRegistration<P>.() -> Unit) = registration.apply(block)

    val outboxes by lazy { registration.outboxes.values }

    val agents by lazy { registration.agents }
}
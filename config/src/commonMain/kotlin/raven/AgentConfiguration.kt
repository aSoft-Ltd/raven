package raven

class AgentConfiguration<P>(
    val params: Map<String, String>,
    val outbox: Outbox<P>?
)
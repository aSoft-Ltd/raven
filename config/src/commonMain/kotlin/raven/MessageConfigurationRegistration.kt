package raven

class MessageConfigurationRegistration<P>(private val configuration: MessageConfiguration) {
    @PublishedApi
    internal val outboxes = mutableMapOf<String, (Map<String, String>) -> Outbox<P>>()

    fun outbox(type: String, block: (Map<String, String>) -> Outbox<P>) {
        outboxes[type] = block
    }

    internal val agents = mutableMapOf<String, (AgentConfiguration<P>) -> Sender<P>>()
    fun agent(type: String, block: (conf: AgentConfiguration<P>) -> Sender<P>) {
        agents[type] = block
    }

    fun buildAgents(): List<Sender<P>> {
        val boxes = buildMap {
            configuration.outbox.forEach {
                val type = it["type"] ?: throw IllegalArgumentException("An outbox must have a defined type")
                val builder = outboxes[type] ?: throw IllegalArgumentException("Factory of outbox with type $type is not configured")
                val name = it["name"] ?: type
                put(name, builder(it))
            }
        }

        return buildList {
            configuration.agent.forEach {
                val type = it["type"] ?: throw IllegalArgumentException("An agent must have a defined type")
                val builder = agents[type] ?: throw IllegalArgumentException("A factory of agent (type=$type) is not configured")
                val outbox = boxes[it["outbox"]]
                val params = AgentConfiguration(it, outbox)
                add(builder(params))
            }
        }
    }
}
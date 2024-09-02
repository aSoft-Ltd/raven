package raven

class MessageConfigurationRegistration<P>(private val configuration: MessageConfiguration<P>) {

    internal class Factory<P> {
        val outboxes = mutableMapOf<String, (Map<String, String>) -> Outbox<P>>()
        val agents = mutableMapOf<String, (AgentConfiguration<P>) -> Sender<P>>()
    }

    private val factory by lazy { Factory<P>() }

    fun outbox(type: String, block: (Map<String, String>) -> Outbox<P>) {
        factory.outboxes[type] = block
    }


    fun agent(type: String, block: (conf: AgentConfiguration<P>) -> Sender<P>) {
        factory.agents[type] = block
    }

    val outboxes by lazy {
        buildMap {
            configuration.outbox.forEach {
                val type = it["type"] ?: throw IllegalArgumentException("An outbox must have a defined type")
                val builder = factory.outboxes[type] ?: throw IllegalArgumentException("Factory of outbox with type $type is not configured")
                val name = it["name"] ?: type
                put(name, builder(it))
            }
        }
    }

    val agents by lazy {
        buildList {
            configuration.agent.forEach {
                val type = it["type"] ?: throw IllegalArgumentException("An agent must have a defined type")
                val builder = factory.agents[type] ?: throw IllegalArgumentException("A factory of agent (type=$type) is not configured")
                val outbox = outboxes[it["outbox"]]
                val params = AgentConfiguration(it, outbox)
                add(builder(params))
            }
        }
    }
}
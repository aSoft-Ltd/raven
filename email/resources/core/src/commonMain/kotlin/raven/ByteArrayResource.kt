package raven

class ByteArrayResource(
    override val name: String,
    override val type: String,
    override val id: String = name,
    private val content: suspend () -> ByteArray
) : EmbeddedResource {
    override suspend fun read() = content()

    fun copy(
        name: String = this.name,
        id: String = this.id
    ) = ByteArrayResource(name, type, id, content)
}
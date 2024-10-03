package raven

import koncurrent.Later

class ByteArrayResource(
    override val name: String,
    override val type: String,
    override val id: String = name,
    private val content: () -> ByteArray
) : EmbeddedResource {
    override fun read() = Later(content())

    fun copy(
        name: String = this.name,
        id: String = this.id
    ) = ByteArrayResource(name, type, id, content)
}
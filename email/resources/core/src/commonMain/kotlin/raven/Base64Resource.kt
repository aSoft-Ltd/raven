package raven

import kotlin.io.encoding.Base64

class Base64Resource(
    override val name: String,
    override val type: String,
    override val id: String = name,
    private val content: () -> String
) : EmbeddedResource {

    override suspend fun read() = Base64.decode(content())

    fun copy(
        name: String = this.name,
        id: String = this.id
    ) = Base64Resource(name, type, id, content)
}
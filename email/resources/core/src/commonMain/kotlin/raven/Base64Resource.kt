package raven

import koncurrent.Later
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class Base64Resource(
    override val name: String,
    override val type: String,
    override val id: String = name,
    private val content: () -> String
) : EmbeddedResource {

    @OptIn(ExperimentalEncodingApi::class)
    override fun read() = Later(Base64.decode(content()))

    fun copy(
        name: String = this.name,
        id: String = this.id
    ) = Base64Resource(name, type, id, content)
}
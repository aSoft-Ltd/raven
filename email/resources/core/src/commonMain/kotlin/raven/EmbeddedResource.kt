package raven

import koncurrent.Later

interface EmbeddedResource {
    val name: String
    val type: String
    val id: String
    fun read(): Later<ByteArray>
}
package raven

interface EmbeddedResource {
    val name: String
    val type: String
    val id: String
    suspend fun read(): ByteArray
}
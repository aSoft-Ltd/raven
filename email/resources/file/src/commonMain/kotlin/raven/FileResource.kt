package raven

import kiota.File
import kiota.FileManager

class FileResource(
    val file: File,
    override val type: String,
    name: String? = null,
    id: String? = name,
    val files: FileManager
) : EmbeddedResource {
    val info by lazy { files.info(file) }
    override val name by lazy { name ?: info.name(extension = true) }
    override val id by lazy { id ?: name ?: info.name(extension = true) }
    override suspend fun read(): ByteArray = files.readBytes(file)
}
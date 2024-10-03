package raven

import epsilon.FileReader
import epsilon.RawFile
import epsilon.RawFileInfo
import epsilon.SystemFileReader
import koncurrent.Later

class FileResource(
    val file: RawFile,
    override val type: String,
    name: String? = null,
    id: String? = name,
    val reader: FileReader = SystemFileReader()
) : EmbeddedResource {
    val info by lazy { RawFileInfo(file) }
    override val name by lazy { name ?: info.nameWithExtension }
    override val id by lazy { id ?: name ?: info.nameWithExtension }
    override fun read(): Later<ByteArray> = reader.read(file)
}
package raven

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SendSmsParams(
    val from: String,
    val to: List<String>,
    val body: String
) {
    constructor(from: String, to: String, body: String) : this(from, listOf(to), body)
}
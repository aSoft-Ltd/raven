package raven

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val email: String,
    val name: String? = null
)
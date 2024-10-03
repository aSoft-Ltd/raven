package raven.internal

import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.content.PartData

class PartBuilder internal constructor() {
    private val parts = mutableListOf<FormPart<*>>()


    /**
     * Appends a pair [key]:[value] with optional [headers].
     */
    public fun append(key: String, value: String, headers: Headers = Headers.Empty) {
        parts += FormPart(key, value, headers)
    }

    /**
     * Appends a pair [key]:[value] with optional [headers].
     */
    public fun append(key: String, value: ByteArray, headers: Headers = Headers.Empty) {
        parts += FormPart(key, value, headers)
    }

    /**
     * Appends a pair [key]:[values] with optional [headers].
     */
    public fun append(key: String, values: Iterable<String>, headers: Headers = Headers.Empty) {
        require(key.endsWith("[]")) {
            "Array parameter must be suffixed with square brackets ie `$key[]`"
        }
        values.forEach { value ->
            parts += FormPart(key, value, headers)
        }
    }

    /**
     * Appends a pair [key]:[values] with optional [headers].
     */
    public fun append(key: String, values: Array<String>, headers: Headers = Headers.Empty) {
        return append(key, values.asIterable(), headers)
    }


    /**
     * Appends a form [part].
     */
    public fun <T : Any> append(part: FormPart<T>) {
        parts += part
    }

    internal fun build(): List<FormPart<*>> = parts
}

suspend fun multipart(block: suspend PartBuilder.() -> Unit): List<PartData> =
    formData(*PartBuilder().apply { block() }.build().toTypedArray())
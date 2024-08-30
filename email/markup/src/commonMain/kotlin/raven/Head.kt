package raven

class Head(
    val meta: MutableList<Meta>
)

class HeadScope(private val head: Head) {

    fun meta(vararg props: Pair<String, String>): Meta {
        val m = Meta(props.toList())
        head.meta.add(m)
        return m
    }

    fun contentType(content: String = "text/html; charset=UTF-8") = meta(
        "http-equiv" to "Content-Type", "content" to content
    )

    fun viewport(content: String = "width=device-width, initial-scale=1") = meta(
        "name" to "viewport", "content" to content
    )
}
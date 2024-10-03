package raven

class Head(
    val meta: MutableList<Meta>,
    val link: MutableList<Link>
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

    fun link(href: String, rel: String = "stylesheet"): Link {
        val l = Link(props = listOf("href" to href, "rel" to rel))
        head.link.add(l)
        return l
    }
}
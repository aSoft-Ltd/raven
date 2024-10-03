package raven

class Html(
    val lang: String,
    val dir: String,
    val head: Head,
    val body: Body
)

class HtmlScope {
    internal var head = Head(mutableListOf(), mutableListOf())
    internal var body = Body(mutableMapOf(), mutableListOf())
    fun head(builder: HeadScope.() -> Unit): Head {
        val scope = HeadScope(head)
        scope.builder()
        return head
    }

    fun body(styles: Styles = css(), builder: ComponentScope<Body>.() -> Unit): Body {
        val default = css().width("100%").margin("0").padding("0")
        body.styles.putAll((default + styles).definitions)
        return scopeOf(body).also(builder).parent
    }
}
package raven

sealed interface FertileComponent : Component {
    val children: MutableList<Component>
}

private val defaultContainerProps = mapOf(
    "align" to "center",
    "width" to "100%",
    "border" to "0",
    "cellPadding" to "0",
    "cellSpacing" to "0",
    "role" to "presentation"
)

interface ComponentScope<out C : FertileComponent> {
    val parent: C
    val css get() = css()
    val center get() = css.text(align = "center")
    fun text(content: String) = text(css()) { content }

    fun text(style: Styles, content: String) = text(style) { content }

    fun text(style: Styles, builder: () -> String) {
        val default = css()
            .font(size = "16px")
            .line(height = "24px")
        parent.children.add(Text((default + style).definitions, builder()))
    }

    fun p(style: Styles = css(), builder: ComponentScope<Paragraph>.() -> Unit) {
        val default = css().margin(top = "16px")
        val p = Paragraph((default + style).definitions, mutableListOf())
        parent.children.add(scopeOf(p).also(builder).parent)
    }

    fun img(src: String, alt: String, width: String? = null, height: String? = null, style: Styles = css()) {
        val props = buildMap {
            put("src", src)
            put("alt", alt)
            if (width != null) put("width", width)
            if (height != null) put("height", height)
        }
        val default = css()
            .display("block")
            .outline("none")
            .border("none")
            .text(decoration = "none")
        val i = Image(props, (default + style).definitions)
        parent.children.add(i)
    }

    fun a(href: String, target: String = "_blank", style: Styles = css(), builder: ComponentScope<A>.() -> Unit) {
        val props = mapOf("href" to href, "target" to target)
        val default = css().text(decoration = "none")
        val l = A(props, (default + style).definitions, mutableListOf())
        parent.children.add(scopeOf(l).also(builder).parent)
    }

    fun label(style: Styles = css(), content: String) {
        val s = css()
            .max(width = "100%")
            .display("inline-block")
            .line(height = "100%")
            .text(decoration = "none")
            .margin("14px", "0")
        text(s + style) { content }
    }

    fun label(content: String): Unit = label(css(), content)

    fun button(
        style: Styles = css(),
        href: String,
        builder: ComponentScope<Button>.() -> Unit
    ) {
        val props = mapOf("href" to href)
        val s = css()
            .line(height = "100%")
            .text(decoration = "none")
            .display("inline-block")
            .max(width = "100%")
            .padding(h = "16px")
            .border(radius = "8px")
        val b = Button(props, (s + style).definitions, mutableListOf())
        parent.children.add(scopeOf(b).also(builder).parent)
    }

    fun br() = parent.children.add(Break)

    fun container(style: Styles = css(), builder: ComponentScope<Container>.() -> Unit) {
        val c = Container(defaultContainerProps, style.definitions, mutableListOf())
        parent.children.add(scopeOf(c).also(builder).parent)
    }

    fun row(style: Styles = css(), builder: ComponentScope<Row>.() -> Unit) {
        val r = Row(defaultContainerProps, style.definitions, mutableListOf())
        parent.children.add(scopeOf(r).also(builder).parent)
    }

    fun col(style: Styles = css(), builder: ComponentScope<Column>.() -> Unit) {
        val c = Column(mapOf(), style.definitions, mutableListOf())
        parent.children.add(scopeOf(c).also(builder).parent)
    }
}
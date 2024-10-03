package raven

class Styles(val definitions: Map<String, String>) {

    private fun put(name: String, value: String): Styles {
        if (value.isBlank()) return this
        return Styles(definitions + (name to value))
    }

    fun display(value: String) = put("display", value)

    fun position(value: String) = put("position", value)

    fun top(value: String) = put("top", value)

    fun bottom(value: String) = put("bottom", value)

    fun left(value: String) = put("left", value)

    fun right(value: String) = put("right", value)

    fun outline(value: String) = put("outline", value)

    fun border(
        width: String = "",
        style: String = "",
        color: String = "",
        radius: String = ""
    ) = this
        .put("border-width", width)
        .put("border-style", style)
        .put("border-color", color)
        .put("border-radius", radius)

    fun text(
        decoration: String = "",
        align: String = ""
    ) = this
        .put("text-decoration", decoration)
        .put("text-align", align)

    fun color(value: String) = put("color", value)

    fun background(
        color: String = "",
        image: String = ""
    ) = this
        .put("background-color", color)
        .put("background-image", image)

    fun width(value: String) = put("width", value)

    fun height(value: String) = put("height", value)

    fun max(
        width: String = "",
        height: String = ""
    ) = this
        .put("max-width", width)
        .put("max-height", height)

    fun line(height: String) = put("line-height", height)

    fun margin(
        top: String = "",
        right: String = "",
        left: String = "",
        bottom: String = ""
    ) = this
        .put("margin-top", top)
        .put("margin-bottom", bottom)
        .put("margin-left", left)
        .put("margin-right", right)

    fun margin(v: String, h: String) = margin(top = v, bottom = v, left = h, right = h)

    fun margin(value: String) = put("margin", value)

    fun padding(value: String) = put("padding", value)

    fun padding(v: String = "", h: String = "") = padding(top = v, bottom = v, left = h, right = h)

    fun padding(
        top: String = "",
        right: String = "",
        left: String = "",
        bottom: String = ""
    ) = this
        .put("padding-top", top)
        .put("padding-bottom", bottom)
        .put("padding-left", left)
        .put("padding-right", right)

    fun font(
        size: String = "",
        weight: String = "",
        family: String = ""
    ) = this
        .put("font-size", size)
        .put("font-weight", weight)
        .put("font-family", family)

    operator fun plus(other: Styles) = Styles(definitions + other.definitions)
}
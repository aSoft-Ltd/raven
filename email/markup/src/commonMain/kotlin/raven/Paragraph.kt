package raven

class Paragraph(
    val styles: Map<String,String>,
    override val children: MutableList<Component>
) : FertileComponent
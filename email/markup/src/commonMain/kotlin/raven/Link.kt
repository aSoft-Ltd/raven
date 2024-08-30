package raven

class Link(
    val props: Map<String,String>,
    val styles: Map<String,String>,
    override val children: MutableList<Component>
) : FertileComponent
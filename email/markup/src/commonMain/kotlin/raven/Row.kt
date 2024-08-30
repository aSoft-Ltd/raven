package raven

class Row(
    val props: Map<String,String>,
    val styles: Map<String,String>,
    override val children: MutableList<Component>
) : FertileComponent
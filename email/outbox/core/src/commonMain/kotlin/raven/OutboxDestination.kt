package raven

class OutboxDestination(
    private val root: String
) {
    fun store() = "$root/store"

    fun sent(to: String) = "$root/$to"
    
    fun delete(receiver: String) = "$root/$receiver"
}
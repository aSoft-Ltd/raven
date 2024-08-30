package raven

class CreditWarning(
    val to: String,
    val limit: Int,
    val message: (count: Int) -> String
)
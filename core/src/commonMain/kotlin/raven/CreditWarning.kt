package raven

class CreditWarning(
    val to: List<String>,
    val limit: Int,
    val message: (count: Int) -> String
)
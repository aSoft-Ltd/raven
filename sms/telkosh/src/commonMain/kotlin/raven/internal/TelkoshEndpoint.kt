package raven.internal

internal class TelkoshEndpoint(private val root: String) {
//    val message by lazy { "$root/sendurlcomma.aspx" }
    val message by lazy { "$root/sendsms_api_json.aspx" }
    val balance by lazy { "$root/balance.aspx" }
}
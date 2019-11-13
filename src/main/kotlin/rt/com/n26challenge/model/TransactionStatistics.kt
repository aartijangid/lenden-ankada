package rt.com.n26challenge.model

data class TransactionStatistics(
    val timestamp: Long = 0,
    val sum: Double = 0.0,
    val min: Double = 0.0,
    val max: Double = 0.0,
    var count: Int = 0
)
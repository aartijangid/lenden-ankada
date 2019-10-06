package rt.com.n26challenge.service

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository

@Service
class TransactionService(
        @Autowired val statisticRepository: TimelyTransactionStatisticsRepository,
        @Autowired val timelyStatisticsComputer: TimelyStatisticsComputer
) {

    fun addTransaction(transaction: Transaction) {
        val timelyTransactionStatistics = timelyStatisticsComputer.compute(transaction)
        statisticRepository.add(timelyStatisticsComputer.computeIndex(transaction.timestamp), timelyTransactionStatistics)
    }
}

data class TimelyTransactionStatistics(
        var timestampIndex: Int = 0,
        var timestamp: Long = 0,
        var sum: Double = 0.0,
        var min: Double = 0.0,
        var max: Double = 0.0,
        var count: Int = 0
)

open class Transaction(var amount: Double, var timestamp: Long) {
    override fun equals(other: Any?): Boolean =
            if (other is Transaction) {
                amount == other.amount &&
                        timestamp == other.timestamp
            } else {
                false
            }

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}

@JsonFormat
data class TransactionRequest(
        val amount: Double,
        val timestamp: Long
)

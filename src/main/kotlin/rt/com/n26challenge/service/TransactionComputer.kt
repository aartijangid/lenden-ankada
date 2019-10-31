package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.repository.TransactionRepository

@Service
class TransactionComputer(
    private val repository: TransactionRepository,
    @Value("\${service.transaction.retain-period-in-second}") private val retainPeriodInSeconds: Int
) {

    fun compute(transaction: Transaction): TransactionStatistics {

        val timestampIndex = computeIndex(timestamp = transaction.timestamp)
        val previousTransactionStatistics = repository.search(timestampIndex)

        return if (previousTransactionStatistics.count == 0)
            getStatistics(transaction)
        else
            mergeTransactionStatistics(transaction, previousTransactionStatistics)
    }

    fun computeIndex(timestamp: Long): Int = (timestamp % retainPeriodInSeconds).toInt()

    private fun mergeTransactionStatistics(transaction: Transaction, previousStats: TransactionStatistics) =
        TransactionStatistics(
            sum = transaction.amount + previousStats.sum,
            count = ++previousStats.count,
            max = if (previousStats.max > transaction.amount) previousStats.max else transaction.amount,
            min = if (previousStats.min < transaction.amount) previousStats.min else transaction.amount
        )

    private fun getStatistics(transaction: Transaction) = TransactionStatistics(
        sum = transaction.amount,
        count = 1,
        max = transaction.amount,
        min = transaction.amount
    )
}

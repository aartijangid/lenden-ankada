package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.model.TransactionStatistics
import rt.com.n26challenge.repository.TransactionRepository

@Service
class TransactionComputer(
    private val repository: TransactionRepository,
    @Value("\${service.transaction.period-in-second}") private val periodInSeconds: Int
) {

    fun compute(transaction: Transaction): TransactionStatistics {

        val timestampIndex = computeIndex(timestamp = transaction.timestamp)
        val previousTransactionStatistics = repository.search(timestampIndex)

        return if (previousTransactionStatistics.count == 0)
            getStatistics(transaction)
        else
            mergeTransactionStatistics(transaction, previousTransactionStatistics)
    }

    fun computeIndex(timestamp: Long): Int = (timestamp % periodInSeconds).toInt()

    private fun mergeTransactionStatistics(
        transaction: Transaction,
        previousTransactionStatistics: TransactionStatistics
    ) =
        TransactionStatistics(
            sum = transaction.amount + previousTransactionStatistics.sum,
            timestamp = transaction.timestamp,
            count = ++previousTransactionStatistics.count,
            max = if (previousTransactionStatistics.max > transaction.amount)
                previousTransactionStatistics.max
            else
                transaction.amount,
            min = if (previousTransactionStatistics.min < transaction.amount)
                previousTransactionStatistics.min
            else
                transaction.amount
        )

    private fun getStatistics(transaction: Transaction) = TransactionStatistics(
        timestamp = transaction.timestamp,
        sum = transaction.amount,
        count = 1,
        max = transaction.amount,
        min = transaction.amount
    )
}

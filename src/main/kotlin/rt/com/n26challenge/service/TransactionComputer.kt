package rt.com.n26challenge.service

import org.springframework.stereotype.Service
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.repository.TransactionRepository

@Service
class TransactionComputer(private val repository: TransactionRepository) {

    fun compute(transaction: Transaction): TransactionStatistics {

        val timestampIndex = computeIndex(timestamp = transaction.timestamp)
        val previousTransactionStatistics = repository.search(timestampIndex)

        return if (previousTransactionStatistics.count == 0)
            getStatistics(transaction)
        else
            mergeTransactionStatistics(transaction, previousTransactionStatistics)
    }

    fun computeIndex(timestamp: Long): Int = (timestamp % 60).toInt()

    fun mergeTransactionStatistics(transaction: Transaction, previousTransactionStatistics: TransactionStatistics) =
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

    fun getStatistics(transaction: Transaction) = TransactionStatistics(
        timestamp = transaction.timestamp,
        sum = transaction.amount,
        count = 1,
        max = transaction.amount,
        min = transaction.amount
    )
}

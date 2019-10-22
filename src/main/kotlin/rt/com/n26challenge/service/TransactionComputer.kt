package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.repository.TransactionRepository

@Service
class TransactionComputer(@Autowired val repository: TransactionRepository) {

    fun compute(transaction: Transaction): TimelyTransactionStatistics {

        val timestampIndex = computeIndex(timestamp = transaction.timestamp)
        val previousTransactionStatistics = repository.search(timestampIndex)

        if (previousTransactionStatistics.count == 0)
            return TimelyTransactionStatistics(timestampIndex = timestampIndex,
                    timestamp = transaction.timestamp,
                    sum = transaction.amount,
                    count = 1,
                    max = transaction.amount,
                    min = transaction.amount)
        else
            return TimelyTransactionStatistics(
                    timestampIndex = timestampIndex,
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
                        transaction.amount)
    }

    fun computeIndex(timestamp: Long): Int = (timestamp % 60).toInt()

}

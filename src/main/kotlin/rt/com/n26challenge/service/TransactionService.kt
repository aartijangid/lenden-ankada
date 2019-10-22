package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rt.com.n26challenge.exception.TransactionException
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository


@Service
class TransactionService(
        @Autowired val statisticRepository: TimelyTransactionStatisticsRepository,
        @Autowired val timelyStatisticsComputer: TimelyStatisticsComputer
) {

    fun addTransaction(transaction: Transaction) {
        if (timelyStatisticsComputer.validate(transaction.timestamp)) {
            val timelyTransactionStatistics = timelyStatisticsComputer.compute(transaction)
            statisticRepository.add(timelyStatisticsComputer.computeIndex(transaction.timestamp), timelyTransactionStatistics)
        } else
            throw TransactionException("OutdatedTransaction")
    }
}

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


package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository

@Service
class TimelyStatisticsComputer(@Autowired val statisticsRepository: TimelyTransactionStatisticsRepository) {

    fun compute(transaction: Transaction): TimelyTransactionStatistics {

        val timestampIndex = computeIndex(timestamp = transaction.timestamp)
        val previousTransactionStatistics = statisticsRepository.search(timestampIndex)!!
        return TimelyTransactionStatistics(
                timestampIndex = timestampIndex,
                sum = transaction.amount + previousTransactionStatistics.sum,
                timestamp = transaction.timestamp,
                count = ++previousTransactionStatistics.count,
                max = transaction.amount,
                min = transaction.amount)
    }

    fun computeIndex(timestamp: Long): Int = (timestamp % 60).toInt()
}

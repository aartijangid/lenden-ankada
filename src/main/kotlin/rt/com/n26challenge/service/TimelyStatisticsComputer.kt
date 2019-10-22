package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class TimelyStatisticsComputer(@Autowired val statisticsRepository: TimelyTransactionStatisticsRepository) {

    fun compute(transaction: Transaction): TimelyTransactionStatistics {

        val timestampIndex = computeIndex(timestamp = transaction.timestamp)
        var previousTransactionStatistics = statisticsRepository.search(timestampIndex)

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

    fun validate(timestamp: Long): Boolean {
        println("in validate $timestamp")
        val utcTimeZone = ZonedDateTime.now(ZoneOffset.UTC)
        val currentTimestamp = utcTimeZone.toEpochSecond()
        val timestampInSeconds = timestamp / 1000
        println(currentTimestamp.minus(timestampInSeconds) in 0..59)
        return currentTimestamp.minus(timestampInSeconds) in 0..59
    }
}

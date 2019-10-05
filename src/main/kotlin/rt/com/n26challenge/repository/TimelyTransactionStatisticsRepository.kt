package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TimelyTransactionStatistics

@Repository
class TimelyTransactionStatisticsRepository {
    var size: Int = 0

    val timelyStatistics: Array<TimelyTransactionStatistics?> = arrayOfNulls<TimelyTransactionStatistics>(2)
    // add
    fun add(index: Int, timelyTransactionStatistics: TimelyTransactionStatistics) {
        timelyStatistics[index] = timelyTransactionStatistics
        size++
    }
    // delete
    fun delete(index: Int) {
        timelyStatistics[index] = null
        size--
    }

    // search
    fun search(index: Int): TimelyTransactionStatistics? = timelyStatistics[index]
}

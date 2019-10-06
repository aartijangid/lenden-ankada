package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TimelyTransactionStatistics

@Repository
class TimelyTransactionStatisticsRepository {
    companion object {
        lateinit var timelyStatistics: Array<TimelyTransactionStatistics?>
    }

    init {
        timelyStatistics = (1..60).map { TimelyTransactionStatistics() }.toTypedArray()
    }

    // add
    fun add(index: Int, timelyTransactionStatistics: TimelyTransactionStatistics) {
        timelyStatistics[index] = timelyTransactionStatistics
    }

    // delete
    fun delete(index: Int) {
        timelyStatistics[index] = TimelyTransactionStatistics()
    }

    // search
    fun search(index: Int): TimelyTransactionStatistics? = timelyStatistics[index]
}

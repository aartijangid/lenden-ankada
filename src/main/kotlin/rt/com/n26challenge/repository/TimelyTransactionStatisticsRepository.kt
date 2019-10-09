package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TimelyTransactionStatistics
import java.util.*

@Repository
class TimelyTransactionStatisticsRepository {
    companion object {
        lateinit var timelyStatistics: Array<TimelyTransactionStatistics>
    }

    init {
        timelyStatistics = (1..60).map { TimelyTransactionStatistics() }.toTypedArray()
    }

    fun add(index: Int, timelyTransactionStatistics: TimelyTransactionStatistics) {
        timelyStatistics[index] = timelyTransactionStatistics
    }

    fun delete(index: Int) {
        timelyStatistics[index] = TimelyTransactionStatistics()
    }

    fun search(index: Int): TimelyTransactionStatistics = timelyStatistics[index]

    fun sum(): Double = timelyStatistics.toList().sumByDouble { it.sum }

    fun count(): Int = timelyStatistics.toList().sumBy { it.count }

    fun min(): Double = Collections.min((timelyStatistics.filter { it.count > 0 }).map { it.min })

    fun max(): Double = Collections.max((timelyStatistics.filter { it.count > 0 }).map { it.max })

    fun avg(): Double = sum().div(count())
}

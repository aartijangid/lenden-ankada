package rt.com.n26challenge.repository

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TimelyTransactionStatistics
import java.time.Instant
import java.util.*


@Repository
class TimelyTransactionStatisticsRepository(private val interval: Long = 60) {

    companion object {
        lateinit var timelyStatistics: Array<TimelyTransactionStatistics>
    }

    init {
        timelyStatistics = (1..60).map { TimelyTransactionStatistics() }.toTypedArray()
    }

    fun add(index: Int, timelyTransactionStatistics: TimelyTransactionStatistics) {
        synchronized(this) {
            timelyStatistics[index] = timelyTransactionStatistics
        }
    }

    fun delete(index: Int) {
        synchronized(this) {
            timelyStatistics[index] = TimelyTransactionStatistics()
        }
    }

    fun search(index: Int): TimelyTransactionStatistics = timelyStatistics[index]

    fun sum(): Double = timelyStatistics.toList().sumByDouble { it.sum }

    fun count(): Int = timelyStatistics.toList().sumBy { it.count }

    fun min(): Double {
        val records = (timelyStatistics.filter { it.count > 0 }).map { it.min }
        return if (records.isEmpty())
            0.0
        else
            Collections.min(records)
    }

    fun max(): Double {
        val records = (timelyStatistics.filter { it.count > 0 }).map { it.max }
        return if (records.isEmpty())
            0.0
        else
            Collections.max(records)
    }

    fun avg(): Double {
        return if (count() > 0)
            sum().div(count())
        else
            return 0.0
    }

    @Scheduled(fixedRate = 1000, initialDelay = 60000)
    fun reinitializeRepositoryIndex() {
        val indexToDiscard = ((Instant.now().epochSecond - 59) % 60).toInt()

        if (timelyStatistics[indexToDiscard].count > 0) {
            timelyStatistics[indexToDiscard] = TimelyTransactionStatistics()
        }
    }
}

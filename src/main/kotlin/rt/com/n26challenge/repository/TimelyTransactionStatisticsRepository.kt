package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TimelyTransactionStatistics
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock


@Repository
class TimelyTransactionStatisticsRepository() {

    var decimalFormat = DecimalFormat("#.##")
    lateinit var lock: ReadWriteLock

    companion object {
        lateinit var timelyStatistics: Array<TimelyTransactionStatistics>
    }

    init {
        timelyStatistics = (1..60).map { TimelyTransactionStatistics() }.toTypedArray()
        this.lock = ReentrantReadWriteLock()
    }

    fun add(index: Int, timelyTransactionStatistics: TimelyTransactionStatistics) {
        try {
            lock.readLock().lock()
            timelyStatistics[index] = timelyTransactionStatistics
        } finally {
            lock.readLock().unlock()
        }
    }

    fun delete(index: Int) {
        try {
            lock.writeLock().lock()
            timelyStatistics[index] = TimelyTransactionStatistics()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun search(index: Int): TimelyTransactionStatistics = timelyStatistics[index]

    fun sum(): Double = decimalFormat.format(timelyStatistics.toList().sumByDouble { it.sum }).toDouble()

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

    fun avg(): Double = if (count() > 0)
        decimalFormat.format(sum().div(count())).toDouble()
    else
        0.0

    fun getTransactionsList(): List<TimelyTransactionStatistics> {
        return timelyStatistics.filter { it.count > 0 }
    }
}

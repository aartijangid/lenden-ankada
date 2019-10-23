package rt.com.n26challenge.service

import org.springframework.stereotype.Service
import rt.com.n26challenge.controller.StatisticsResponse
import rt.com.n26challenge.repository.TransactionRepository
import java.text.DecimalFormat
import java.util.*

@Service
class StatisticsService(val transactionRepository: TransactionRepository) {
    fun getStatistics(): StatisticsResponse {
        val decimalFormat = DecimalFormat("#.##")

        val transactionsList = transactionRepository.getTransactionsList()
        val sum = decimalFormat.format(transactionsList.sumByDouble { it.sum }).toDouble()
        val count = transactionsList.sumBy { it.count }
        val avg = decimalFormat.format(sum.div(count)).toDouble()
        val max = Collections.max(transactionsList.map { it.max })
        val min = Collections.min(transactionsList.map { it.min })

        return StatisticsResponse(sum, avg, max, min, count)

    }
}

data class TransactionStatistics(
        val timestampIndex: Int = 0,
        val timestamp: Long = 0,
        val sum: Double = 0.0,
        val min: Double = 0.0,
        val max: Double = 0.0,
        var count: Int = 0
)

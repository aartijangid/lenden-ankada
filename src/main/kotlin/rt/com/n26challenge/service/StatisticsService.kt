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

        val transactions = transactionRepository.getTransactions()
        val sum = decimalFormat.format(transactions.sumByDouble { it.sum }).toDouble()
        val count = transactions.sumBy { it.count }
        val avg = decimalFormat.format(sum.div(count)).toDouble()
        val max = Collections.max(transactions.map { it.max })
        val min = Collections.min(transactions.map { it.min })

        return StatisticsResponse(
            sum = sum,
            avg = avg,
            max = max,
            min = min,
            count = count
        )

    }
}

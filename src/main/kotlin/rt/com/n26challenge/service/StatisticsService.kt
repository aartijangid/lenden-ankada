package rt.com.n26challenge.service

import org.springframework.stereotype.Service
import rt.com.n26challenge.controller.StatisticsResponse
import rt.com.n26challenge.repository.TransactionRepository

@Service
class StatisticsService(val statisticRepository: TransactionRepository) {
    fun getStatistics(): StatisticsResponse {
        val statisticsResponse = StatisticsResponse(
                statisticRepository.sum(),
                statisticRepository.avg(),
                statisticRepository.max(),
                statisticRepository.min(),
                statisticRepository.count()
        )

        println(statisticsResponse)
        return statisticsResponse

    }
}

data class TimelyTransactionStatistics(
        val timestampIndex: Int = 0,
        val timestamp: Long = 0,
        val sum: Double = 0.0,
        val min: Double = 0.0,
        val max: Double = 0.0,
        var count: Int = 0
)

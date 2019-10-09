package rt.com.n26challenge.service

import org.springframework.stereotype.Service
import rt.com.n26challenge.controller.StatisticsResponse
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository

@Service
class StatisticsService(val statisticRepository: TimelyTransactionStatisticsRepository) {
    fun get(): StatisticsResponse {
        val statisticsResponse = StatisticsResponse()

        statisticsResponse.sum = statisticRepository.sum()
        statisticsResponse.count = statisticRepository.count()
        statisticsResponse.avg = statisticRepository.avg()
        statisticsResponse.min = statisticRepository.min()
        statisticsResponse.max = statisticRepository.max()

        println(statisticsResponse)
        return statisticsResponse

    }
}
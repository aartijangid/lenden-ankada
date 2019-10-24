package rt.com.n26challenge.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import rt.com.n26challenge.service.StatisticsService

@RestController
class StatisticsController(@Autowired val statisticsService: StatisticsService) {

    @GetMapping(value = ["/statistics"])
    fun getStatistics(): StatisticsResponse = statisticsService.getStatistics()
}

data class StatisticsResponse(
        val sum: Double = 0.0,
        val avg: Double = 0.0,
        val max: Double = 0.0,
        val min: Double = 0.0,
        val count: Int = 0
)

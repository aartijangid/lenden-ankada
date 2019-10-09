package rt.com.n26challenge.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import rt.com.n26challenge.service.StatisticsService

@RestController
class StatisticsController(@Autowired val statisticsService: StatisticsService) {

    @GetMapping(value = ["/statistics"])
    fun getStatistics(): StatisticsResponse {

        return statisticsService.get()
    }
}

data class StatisticsResponse(
        var sum: Double = 0.0,
        var avg: Double = 0.0,
        var max: Double = 0.0,
        var min: Double = 0.0,
        var count: Int = 0
)

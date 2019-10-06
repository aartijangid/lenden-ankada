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
        val sum: Int = 0,
        val avg: Int = 0,
        val max: Int = 0,
        val min: Int = 0,
        val count: Int = 0
)

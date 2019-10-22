package rt.com.n26challenge.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.controller.StatisticsResponse
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class StatisticsServiceTest {

    private lateinit var statisticsService: StatisticsService

    private lateinit var transaction1: TimelyTransactionStatistics
    private lateinit var transaction2: TimelyTransactionStatistics

    @Mock
    lateinit var statisticsRepository: TimelyTransactionStatisticsRepository

    @BeforeEach
    fun setUp() {
        statisticsService = StatisticsService(statisticRepository = statisticsRepository)
        transaction1 = TimelyTransactionStatistics(
                timestamp = Instant.now().epochSecond,
                timestampIndex = 0,
                sum = 7.0,
                min = 7.0,
                max = 7.0,
                count = 1
        )
        transaction2 = TimelyTransactionStatistics(
                timestamp = Instant.now().epochSecond.plus(1),
                timestampIndex = 1,
                sum = 3.0,
                min = 3.0,
                max = 3.0,
                count = 1
        )
    }

    @Test
    fun `get should return computed statistics for last 60 seconds`() {
        //given
        val expectedStatisticsResponse = StatisticsResponse(
                sum = 10.0,
                count = 2,
                max = 7.0,
                min = 3.0,
                avg = 5.0
        )

        given(statisticsRepository.sum()).willReturn(10.0)
        given(statisticsRepository.count()).willReturn(2)
        given(statisticsRepository.max()).willReturn(7.0)
        given(statisticsRepository.min()).willReturn(3.0)
        given(statisticsRepository.avg()).willReturn(5.0)

        // then
        assertEquals(expectedStatisticsResponse, statisticsService.getStatistics())

    }
}
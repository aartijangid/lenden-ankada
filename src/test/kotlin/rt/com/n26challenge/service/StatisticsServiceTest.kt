package rt.com.n26challenge.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.controller.StatisticsResponse
import rt.com.n26challenge.model.TransactionStatistics
import rt.com.n26challenge.repository.TransactionRepository
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class StatisticsServiceTest {

    private lateinit var statisticsService: StatisticsService

    private lateinit var transactionStatistics1: TransactionStatistics
    private lateinit var transactionStatistics2: TransactionStatistics

    @Mock
    lateinit var transactionRepository: TransactionRepository

    @BeforeEach
    fun setUp() {
        statisticsService = StatisticsService(transactionRepository = transactionRepository)
        transactionStatistics1 = TransactionStatistics(
            timestamp = Instant.now().epochSecond,
            min = 3.0,
            max = 7.0,
            sum = 10.0,
            count = 2
        )

        transactionStatistics2 = TransactionStatistics(
            timestamp = Instant.now().epochSecond,
            min = 5.0,
            max = 8.0,
            sum = 13.0,
            count = 2
        )
    }

    @Test
    fun `get should return computed statistics for last 60 seconds`() {
        //given
        val expectedStatisticsResponse = StatisticsResponse(
            sum = 23.0,
            count = 4,
            max = 8.0,
            min = 3.0,
            avg = 5.75
        )

        given(transactionRepository.getTransactions()).willReturn(
            listOf(
                transactionStatistics1,
                transactionStatistics2
            )
        )

        // then
        assertEquals(expectedStatisticsResponse, statisticsService.getStatistics())

    }
}
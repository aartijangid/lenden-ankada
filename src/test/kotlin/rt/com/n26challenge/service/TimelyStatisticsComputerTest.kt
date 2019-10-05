package rt.com.n26challenge.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class TimelyStatisticsComputerTest {

    private lateinit var timelyStatisticsComputer: TimelyStatisticsComputer
    private var currentTimestamp = Instant.EPOCH.plusSeconds(60).epochSecond

    @Mock
    lateinit var statisticsRepository: TimelyTransactionStatisticsRepository

    @BeforeEach
    fun setUp() {
        timelyStatisticsComputer = TimelyStatisticsComputer(statisticsRepository = statisticsRepository)

    }

    @Test
    fun `computeIndex - given current timestamp should return index zero`() {
        // then
        assertEquals(0, timelyStatisticsComputer.computeIndex(timestamp = currentTimestamp))
    }

    @Test
    fun `computeIndex - given timestamp for one second later should return index one`() {
        // given
        var currentTimestamp = currentTimestamp.plus(1)

        // then
        assertEquals(1, timelyStatisticsComputer.computeIndex(timestamp = currentTimestamp))
    }

    @Test
    fun `computeIndex - given 59th second should return index 59`() {
        // given
        var currentTimestamp = currentTimestamp.plus(59)

        // then
        assertEquals(59, timelyStatisticsComputer.computeIndex(timestamp = currentTimestamp))
    }

    @Test
    fun `compute - given transaction should return computed timely transaction statistics`() {
        // given
        val transaction = Transaction(amount = 5.0, timestamp = currentTimestamp)
        val existingTransaction = TimelyTransactionStatistics()
        val expectedTimelyTransactionStatistics = TimelyTransactionStatistics(
                timestampIndex = 0,
                sum = 5.0,
                timestamp = transaction.timestamp,
                count = 1,
                max = 5.0,
                min = 5.0
        )
        given(statisticsRepository.search(0)).willReturn(existingTransaction)
        // when
        var actualTimelyTransactionStatistics = timelyStatisticsComputer.compute(transaction = transaction)

        // then
        assertEquals(expectedTimelyTransactionStatistics, actualTimelyTransactionStatistics)
    }

    @Test
    fun `compute - given transaction repository already having one transaction should return computed timely transaction statistics`() {
        // given
        val transaction = Transaction(amount = 5.0, timestamp = currentTimestamp)
        val existingTransaction = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = currentTimestamp,
                sum = 5.0,
                count = 1,
                max = 5.0
                )
        val expectedTimelyTransactionStatistics = TimelyTransactionStatistics(
                timestampIndex = 0,
                sum = 10.0,
                timestamp = transaction.timestamp,
                count = 2,
                max = 5.0,
                min = 5.0
        )
        given(statisticsRepository.search(0)).willReturn(existingTransaction)

        // when
        var actualTimelyTransactionStatistics = timelyStatisticsComputer.compute(transaction = transaction)

        // then
        assertEquals(expectedTimelyTransactionStatistics, actualTimelyTransactionStatistics)
    }
}

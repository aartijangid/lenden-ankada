package rt.com.n26challenge.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rt.com.n26challenge.service.TimelyTransactionStatistics
import java.time.Instant


class TimelyTransactionStatisticsRepositoryTest {

    private lateinit var timelyTransactionStatisticsRepository: TimelyTransactionStatisticsRepository

    @BeforeEach
    fun setUp() {
        timelyTransactionStatisticsRepository = TimelyTransactionStatisticsRepository()
    }

    @Test
    fun `add - should insert the computed timely transaction statistics in the computed index`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics(sum = 123.0)
        // when
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics)
        // then
        assertEquals(timelyTransactionStatistics, timelyTransactionStatisticsRepository.search(1))
    }

    @Test
    fun `delete - should delete the computed timely transaction statistics and decrement the size`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics(sum = 123.0)
        timelyTransactionStatisticsRepository.add(5, timelyTransactionStatistics)
        // when
        timelyTransactionStatisticsRepository.delete(5)
        // then
        assertEquals(TimelyTransactionStatistics(), timelyTransactionStatisticsRepository.search(5))
    }

    @Test
    fun `search - should search by index for the inserted transaction statistics`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics(
                timestamp = 1234567890,
                sum = 5.0
        )
        // when
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics)
        // then
        assertEquals(timelyTransactionStatistics, timelyTransactionStatisticsRepository.search(1))
    }

    @Test
    fun `search - should search by index for non-existing transaction and return default timely transaction statistics`() {
        // given
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics)
        // when
        timelyTransactionStatisticsRepository.delete(0)
        // then
        assertEquals(TimelyTransactionStatistics(), timelyTransactionStatisticsRepository.search(0))
    }

    @Test
    fun `search - when null transaction should return default timely transaction statistics`() {
        // given
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        // then
        assertEquals(timelyTransactionStatistics, timelyTransactionStatisticsRepository.search(0))
    }

    @Test
    fun `on start up size of timely transaction statistics repository should be 60`() {
        assertEquals(60, TimelyTransactionStatisticsRepository.timelyStatistics.size)
    }

    @Test
    fun `sum - should return sum of all the transactions in timely transaction statistics repository`() {
        // given
        val timelyTransactionStatistics1 = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val timelyTransactionStatistics2 = TimelyTransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics1)
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics2)

        assertEquals(23.0, timelyTransactionStatisticsRepository.sum())
    }

    @Test
    fun `count - should return number of all the transactions count in timely transaction statistics repository`() {
        // given
        val timelyTransactionStatistics1 = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val timelyTransactionStatistics2 = TimelyTransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics1)
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics2)

        assertEquals(4, timelyTransactionStatisticsRepository.count())
    }

    @Test
    fun `min - should return minimum of all the transactions in timely transaction statistics repository`() {
        // given
        val timelyTransactionStatistics1 = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val timelyTransactionStatistics2 = TimelyTransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics1)
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics2)

        assertEquals(3.0, timelyTransactionStatisticsRepository.min())
    }

    @Test
    fun `max - should return maximum of all the transactions in timely transaction statistics repository`() {
        // given
        val timelyTransactionStatistics1 = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val timelyTransactionStatistics2 = TimelyTransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics1)
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics2)

        assertEquals(8.0, timelyTransactionStatisticsRepository.max())
    }

    @Test
    fun `avg - should return average transaction amount of all the transactions in timely transaction statistics repository`() {
        // given
        val timelyTransactionStatistics1 = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val timelyTransactionStatistics2 = TimelyTransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics1)
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics2)

        assertEquals(5.75, timelyTransactionStatisticsRepository.avg())
    }

    @Test
    fun `getTransactionsList - should return filtered list of transactions from timely transaction statistics Repository`() {
        val timelyTransactionStatistics1 = TimelyTransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val timelyTransactionStatistics2 = TimelyTransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics1)
        timelyTransactionStatisticsRepository.add(1, timelyTransactionStatistics2)

        assertEquals(2, timelyTransactionStatisticsRepository.getTransactionsList().size)
    }

}

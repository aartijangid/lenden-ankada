package rt.com.n26challenge.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rt.com.n26challenge.service.TimelyTransactionStatistics

class TimelyTransactionStatisticsRepositoryTest {

    private lateinit var timelyTransactionStatisticsRepository: TimelyTransactionStatisticsRepository

    @BeforeEach
    fun setUp() {
        timelyTransactionStatisticsRepository = TimelyTransactionStatisticsRepository()
    }

    @Test
    fun `add - should insert the computed timely transaction statistics and increment the size`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        // when
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics)
        // then
        assertEquals(1, timelyTransactionStatisticsRepository.size)
    }

    @Test
    fun `delete - should delete the computed timely transaction statistics and decrement the size`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics)
        // when
        timelyTransactionStatisticsRepository.delete(0)
        // then
        assertEquals(0, timelyTransactionStatisticsRepository.size)
    }

    @Test
    fun `add - should insert the computed timely transaction statistics in the computed index`(){
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        // when
        timelyTransactionStatisticsRepository.add(0,timelyTransactionStatistics)
        // then
        assertEquals(timelyTransactionStatistics, timelyTransactionStatisticsRepository.search(0))
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
    fun `search - should search by index for non-existing transaction and return null`() {
        // given
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        timelyTransactionStatisticsRepository.add(0, timelyTransactionStatistics)
        // when
        timelyTransactionStatisticsRepository.delete(0)
        // then
        assertEquals(null, timelyTransactionStatisticsRepository.search(0))
    }
}

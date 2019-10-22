package rt.com.n26challenge.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rt.com.n26challenge.service.TimelyTransactionStatistics
import java.time.Instant


class TransactionRepositoryTest {

    private lateinit var transactionRepository: TransactionRepository

    @BeforeEach
    fun setUp() {
        transactionRepository = TransactionRepository()
    }

    @Test
    fun `add - should insert the computed timely transaction statistics in the computed index`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics(sum = 123.0)
        // when
        transactionRepository.add(1, timelyTransactionStatistics)
        // then
        assertEquals(timelyTransactionStatistics, transactionRepository.search(1))
    }

    @Test
    fun `delete - should delete the computed timely transaction statistics and decrement the size`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics(sum = 123.0)
        transactionRepository.add(5, timelyTransactionStatistics)
        // when
        transactionRepository.delete(5)
        // then
        assertEquals(TimelyTransactionStatistics(), transactionRepository.search(5))
    }

    @Test
    fun `search - should search by index for the inserted transaction statistics`() {
        // give
        val timelyTransactionStatistics = TimelyTransactionStatistics(
                timestamp = 1234567890,
                sum = 5.0
        )
        // when
        transactionRepository.add(1, timelyTransactionStatistics)
        // then
        assertEquals(timelyTransactionStatistics, transactionRepository.search(1))
    }

    @Test
    fun `search - should search by index for non-existing transaction and return default timely transaction statistics`() {
        // given
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        transactionRepository.add(0, timelyTransactionStatistics)
        // when
        transactionRepository.delete(0)
        // then
        assertEquals(TimelyTransactionStatistics(), transactionRepository.search(0))
    }

    @Test
    fun `search - when null transaction should return default timely transaction statistics`() {
        // given
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        // then
        assertEquals(timelyTransactionStatistics, transactionRepository.search(0))
    }

    @Test
    fun `on start up size of timely transaction statistics repository should be 60`() {
        assertEquals(60, TransactionRepository.timelyStatistics.size)
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
        transactionRepository.add(0, timelyTransactionStatistics1)
        transactionRepository.add(1, timelyTransactionStatistics2)

        assertEquals(23.0, transactionRepository.sum())
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
        transactionRepository.add(0, timelyTransactionStatistics1)
        transactionRepository.add(1, timelyTransactionStatistics2)

        assertEquals(4, transactionRepository.count())
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
        transactionRepository.add(0, timelyTransactionStatistics1)
        transactionRepository.add(1, timelyTransactionStatistics2)

        assertEquals(3.0, transactionRepository.min())
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
        transactionRepository.add(0, timelyTransactionStatistics1)
        transactionRepository.add(1, timelyTransactionStatistics2)

        assertEquals(8.0, transactionRepository.max())
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
        transactionRepository.add(0, timelyTransactionStatistics1)
        transactionRepository.add(1, timelyTransactionStatistics2)

        assertEquals(5.75, transactionRepository.avg())
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
        transactionRepository.add(0, timelyTransactionStatistics1)
        transactionRepository.add(1, timelyTransactionStatistics2)

        assertEquals(2, transactionRepository.getTransactionsList().size)
    }

}

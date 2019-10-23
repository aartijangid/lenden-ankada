package rt.com.n26challenge.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rt.com.n26challenge.service.TransactionStatistics
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
        val transactionStatistics = TransactionStatistics(sum = 123.0)
        // when
        transactionRepository.add(1, transactionStatistics)
        // then
        assertEquals(transactionStatistics, transactionRepository.search(1))
    }

    @Test
    fun `delete - should delete the computed timely transaction statistics and decrement the size`() {
        // give
        val transactionStatistics = TransactionStatistics(sum = 123.0)
        transactionRepository.add(5, transactionStatistics)
        // when
        transactionRepository.delete(5)
        // then
        assertEquals(TransactionStatistics(), transactionRepository.search(5))
    }

    @Test
    fun `search - should search by index for the inserted transaction statistics`() {
        // give
        val transactionStatistics = TransactionStatistics(
                timestamp = 1234567890,
                sum = 5.0
        )
        // when
        transactionRepository.add(1, transactionStatistics)
        // then
        assertEquals(transactionStatistics, transactionRepository.search(1))
    }

    @Test
    fun `search - should search by index for non-existing transaction and return default timely transaction statistics`() {
        // given
        val transactionStatistics = TransactionStatistics()
        transactionRepository.add(0, transactionStatistics)
        // when
        transactionRepository.delete(0)
        // then
        assertEquals(TransactionStatistics(), transactionRepository.search(0))
    }

    @Test
    fun `search - when null transaction should return default timely transaction statistics`() {
        // given
        val transactionStatistics = TransactionStatistics()
        // then
        assertEquals(transactionStatistics, transactionRepository.search(0))
    }

    @Test
    fun `on start up size of timely transaction statistics repository should be 60`() {
        assertEquals(60, TransactionRepository.transactionRepository.size)
    }

    @Test
    fun `getTransactionsList - should return filtered list of transactions from timely transaction statistics Repository`() {
        val transactionStatistics1 = TransactionStatistics(
                timestampIndex = 0,
                timestamp = Instant.now().epochSecond,
                min = 3.0,
                max = 7.0,
                sum = 10.0,
                count = 2
        )

        val transactionStatistics2 = TransactionStatistics(
                timestampIndex = 1,
                timestamp = Instant.now().epochSecond,
                min = 5.0,
                max = 8.0,
                sum = 13.0,
                count = 2
        )

        // when
        transactionRepository.add(0, transactionStatistics1)
        transactionRepository.add(1, transactionStatistics2)

        assertEquals(2, transactionRepository.getTransactionsList().size)
    }

}

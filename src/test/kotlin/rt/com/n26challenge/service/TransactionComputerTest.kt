package rt.com.n26challenge.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.repository.TransactionRepository
import java.time.Instant


@ExtendWith(MockitoExtension::class)
internal class TransactionComputerTest {

    private lateinit var transactionComputer: TransactionComputer

    private var currentTimestamp = Instant.EPOCH.plusSeconds(60).toEpochMilli()

    @Mock
    lateinit var repository: TransactionRepository

    @BeforeEach
    fun setUp() {
        transactionComputer = TransactionComputer(repository = repository, periodInSeconds = 60)

    }

    @Test
    fun `computeIndex - given current timestamp should return index zero`() {
        // then
        assertThat(transactionComputer.computeIndex(timestamp = currentTimestamp)).isEqualTo(0)
    }

    @Test
    fun `computeIndex - given timestamp for one second later should return index one`() {
        // given
        val currentTimestamp = currentTimestamp.plus(1)

        // then
        assertThat(transactionComputer.computeIndex(timestamp = currentTimestamp)).isEqualTo(1)
    }

    @Test
    fun `computeIndex - given 59th second should return index 59`() {
        // given
        val currentTimestamp = currentTimestamp.plus(59)

        // then
        assertThat(transactionComputer.computeIndex(timestamp = currentTimestamp)).isEqualTo(59)
    }

    @Test
    fun `compute - given transaction should return computed timely transaction statistics`() {
        // given
        val transaction = Transaction(amount = 5.0, timestamp = currentTimestamp)
        val existingTransaction = TransactionStatistics()
        val expectedTransactionStatistics = TransactionStatistics(
            sum = 5.0,
            timestamp = transaction.timestamp,
            count = 1,
            max = 5.0,
            min = 5.0
        )
        given(repository.search(0)).willReturn(existingTransaction)
        // when
        val actualTransactionStatistics = transactionComputer.compute(transaction = transaction)

        // then
        assertThat(expectedTransactionStatistics).isEqualTo(actualTransactionStatistics)
    }

    @Test
    fun `compute - given transaction repository already having one transaction should return computed timely transaction statistics`() {
        // given
        val transaction = Transaction(amount = 5.0, timestamp = currentTimestamp)
        val existingTransaction = TransactionStatistics(
            timestamp = currentTimestamp,
            sum = 5.0,
            count = 1,
            max = 5.0,
            min = 5.0
        )
        val expectedTransactionStatistics = TransactionStatistics(
            timestamp = currentTimestamp,
            sum = 10.0,
            count = 2,
            max = 5.0,
            min = 5.0
        )
        given(repository.search(0)).willReturn(existingTransaction)

        // when
        val actualTransactionStatistics = transactionComputer.compute(transaction = transaction)

        // then
        assertThat(expectedTransactionStatistics).isEqualTo(actualTransactionStatistics)
    }
}

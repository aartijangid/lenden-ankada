package rt.com.n26challenge.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.exception.TransactionException
import rt.com.n26challenge.fixtures.TransactionType
import rt.com.n26challenge.fixtures.create
import rt.com.n26challenge.model.TransactionStatistics
import rt.com.n26challenge.repository.TransactionRepository

@ExtendWith(MockitoExtension::class)
class TransactionServiceTest {

    private lateinit var transactionService: TransactionService

    @Mock
    lateinit var repository: TransactionRepository

    @Mock
    lateinit var transactionComputer: TransactionComputer

    @BeforeEach
    fun setUp() {
        transactionService = TransactionService(
            statisticRepository = repository,
            transactionComputer = transactionComputer
        )
    }

    @Test
    fun `given transaction should add computed statistics to transaction-statistics-repository`() {
        // given
        val transaction = TransactionType.CURRENT.create()
        val transactionStatistics = TransactionStatistics()
        given(transactionComputer.compute(transaction)).willReturn(transactionStatistics)
        given(transactionComputer.computeIndex(transaction.timestamp)).willReturn(2)

        // when
        transactionService.addTransaction(transaction)

        // then
        val inOrder = inOrder(transactionComputer, repository)
        inOrder.verify(transactionComputer).compute(transaction)
        inOrder.verify(transactionComputer).computeIndex(transaction.timestamp)
        inOrder.verify(repository).add(2, transactionStatistics)
    }

    @Test
    fun `given future transaction should throw exception`() {
        val transaction = TransactionType.FUTURE.create()

        assertThrows<TransactionException> { transactionService.addTransaction(transaction) }
    }
}

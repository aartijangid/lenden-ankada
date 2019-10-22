package rt.com.n26challenge.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.factories.TransactionFactory
import rt.com.n26challenge.factories.TransactionType
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
                transactionComputer = transactionComputer)
    }

    @Test
    fun `given transaction should add computed statistics to transaction-statistics-repository`() {
        // given
        val transaction = TransactionFactory.create(TransactionType.CURRENT)
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        given(transactionComputer.compute(transaction)).willReturn(timelyTransactionStatistics)
        given(transactionComputer.computeIndex(transaction.timestamp)).willReturn(2)

        // when
        transactionService.addTransaction(transaction)

        // then
        val inOrder = Mockito.inOrder(transactionComputer, repository)
        inOrder.verify(transactionComputer).compute(transaction)
        inOrder.verify(transactionComputer).computeIndex(transaction.timestamp)
        inOrder.verify(repository).add(2, timelyTransactionStatistics)
    }
}

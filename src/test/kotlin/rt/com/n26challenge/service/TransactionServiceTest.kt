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
import rt.com.n26challenge.repository.TimelyTransactionStatisticsRepository

@ExtendWith(MockitoExtension::class)
class TransactionServiceTest {

    private lateinit var transactionService: TransactionService

    @Mock
    lateinit var statisticsRepository: TimelyTransactionStatisticsRepository

    @Mock
    lateinit var timelyStatisticsComputer: TimelyStatisticsComputer

    @BeforeEach
    fun setUp() {
        transactionService = TransactionService(
                statisticRepository = statisticsRepository,
                timelyStatisticsComputer = timelyStatisticsComputer)
    }

    @Test
    fun `given transaction should add computed statistics to transaction-statistics-repository`() {
        // given
        val transaction = TransactionFactory.create(TransactionType.CURRENT)
        val timelyTransactionStatistics = TimelyTransactionStatistics()
        given(timelyStatisticsComputer.validate(transaction.timestamp)).willReturn(true)
        given(timelyStatisticsComputer.compute(transaction)).willReturn(timelyTransactionStatistics)
        given(timelyStatisticsComputer.computeIndex(transaction.timestamp)).willReturn(2)

        // when
        transactionService.addTransaction(transaction)

        // then
        val inOrder = Mockito.inOrder(timelyStatisticsComputer, statisticsRepository)
        inOrder.verify(timelyStatisticsComputer).compute(transaction)
        inOrder.verify(timelyStatisticsComputer).computeIndex(transaction.timestamp)
        inOrder.verify(statisticsRepository).add(2, timelyTransactionStatistics)
    }
}

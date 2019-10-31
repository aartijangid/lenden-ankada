package rt.com.n26challenge.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.inOrder
import org.mockito.junit.jupiter.MockitoExtension
import rt.com.n26challenge.repository.TransactionRepository
import java.time.Clock
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class CleanUpServiceTest {
    private lateinit var cleanUpService: CleanUpService
    private lateinit var transactionStatistics: TransactionStatistics

    @Mock
    private lateinit var repository: TransactionRepository

    @BeforeEach
    fun setUp() {
        cleanUpService = CleanUpService(transactionRepository = repository, retainPeriodInSeconds = 60)
        transactionStatistics = TransactionStatistics(
            min = 3.0,
            max = 7.0,
            sum = 10.0,
            count = 2
        )
    }

    @Test
    fun `reinitializeRepositoryIndex - cleanup should be performed`() {
        // given
        val currentIndex = ((Instant.now(Clock.systemUTC()).epochSecond) % 60).toInt()
        given(repository.search(currentIndex)).willReturn(transactionStatistics)
        doNothing().`when`(repository).delete(currentIndex)

        // when
        cleanUpService.reinitializeRepositoryIndex()

        // then
        val inOrder = inOrder(repository)
        inOrder.verify(repository).search(currentIndex)
        inOrder.verify(repository).delete(currentIndex)
    }
}
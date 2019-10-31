package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import rt.com.n26challenge.repository.TransactionRepository
import java.time.Clock
import java.time.Instant

@Service
class CleanUpService(
    private val transactionRepository: TransactionRepository,
    @Value("\${service.transaction.retain-period-in-second}") private val retainPeriodInSeconds: Int
) {

    @Scheduled(fixedRate = 1000, initialDelay = 60000)
    fun reinitializeRepositoryIndex() {
        val indexToDiscard = ((Instant.now(Clock.systemUTC()).epochSecond) % retainPeriodInSeconds).toInt()
        println("Scheduled called...")
        if ((transactionRepository.search(indexToDiscard)).count > 0) {
            transactionRepository.delete(indexToDiscard)
        }
    }
}
package rt.com.n26challenge.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import rt.com.n26challenge.repository.TransactionRepository
import java.time.Clock
import java.time.Instant

@Service
class CleanUpService(
    val transactionRepository: TransactionRepository
) {

    @Scheduled(fixedRate = 1000, initialDelay = 60000)
    fun reinitializeRepositoryIndex() {
        val indexToDiscard = ((Instant.now(Clock.systemUTC()).epochSecond) % 60).toInt()
        println("Scheduled called...")
        if ((transactionRepository.search(indexToDiscard)).count > 0) {
            transactionRepository.delete(indexToDiscard)
        }


    }
}
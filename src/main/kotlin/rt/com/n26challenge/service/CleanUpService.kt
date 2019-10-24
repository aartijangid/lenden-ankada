package rt.com.n26challenge.service

import org.springframework.scheduling.annotation.Scheduled
import rt.com.n26challenge.repository.TransactionRepository
import java.time.Instant

class CleanUpService(val transactionRepository: TransactionRepository) {

    @Scheduled(fixedRate = 1000, initialDelay = 60000)
    fun reinitializeRepositoryIndex() {
        val indexToDiscard = ((Instant.now().epochSecond) % 60).toInt()

        try {
            transactionRepository.lock.writeLock().lock()
            if (TransactionRepository.transactionRepository[indexToDiscard].count > 0) {
                TransactionRepository.transactionRepository[indexToDiscard] = TransactionStatistics()
            }
        } finally {
            transactionRepository.lock.writeLock().unlock()
        }

    }
}
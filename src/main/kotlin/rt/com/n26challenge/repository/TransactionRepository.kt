package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TransactionStatistics
import java.util.concurrent.locks.ReentrantReadWriteLock


@Repository
class TransactionRepository {

    val lock = ReentrantReadWriteLock()

    val transactionRepository = (1..60).map { TransactionStatistics() }.toTypedArray()

    fun add(index: Int, transactionStatistics: TransactionStatistics) {
        try {
            lock.writeLock().lock()
            transactionRepository[index] = transactionStatistics
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun delete(index: Int) = try {
        lock.writeLock().lock()
        transactionRepository[index] = TransactionStatistics()
    } finally {
        lock.writeLock().unlock()
    }

    // There might be inconsistency between when repository is searched and when a value is added.
    // Consider locking.
    fun search(index: Int): TransactionStatistics = transactionRepository[index]

    fun getTransactions(): List<TransactionStatistics> = try {
        lock.readLock().lock()
        transactionRepository.filter { it.count > 0 }
    } finally {
        lock.readLock().unlock()
    }
}

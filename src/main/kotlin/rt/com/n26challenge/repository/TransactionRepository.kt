package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TransactionStatistics
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock


@Repository
class TransactionRepository() {

    private final val lock: ReadWriteLock

    companion object {
        lateinit var transactionRepository: Array<TransactionStatistics>
    }

    init {
        transactionRepository = (1..60).map { TransactionStatistics() }.toTypedArray()
        this.lock = ReentrantReadWriteLock()
    }

    fun add(index: Int, transactionStatistics: TransactionStatistics) {
        try {
            lock.readLock().lock()
            transactionRepository[index] = transactionStatistics
        } finally {
            lock.readLock().unlock()
        }
    }

    fun delete(index: Int) {
        try {
            lock.writeLock().lock()
            transactionRepository[index] = TransactionStatistics()
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun search(index: Int): TransactionStatistics = transactionRepository[index]

    fun getTransactionsList(): List<TransactionStatistics> {
        return transactionRepository.filter { it.count > 0 }
    }
}

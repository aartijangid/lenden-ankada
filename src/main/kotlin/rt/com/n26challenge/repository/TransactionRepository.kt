package rt.com.n26challenge.repository

import org.springframework.stereotype.Repository
import rt.com.n26challenge.service.TransactionStatistics
import java.util.concurrent.locks.ReentrantReadWriteLock


@Repository
class TransactionRepository() {

    val lock = ReentrantReadWriteLock()

    val dataStore = (1..60).map { TransactionStatistics() }.toTypedArray()

    fun add(index: Int, transactionStatistics: TransactionStatistics) = try {
        lock.writeLock().lock()
        dataStore[index] = transactionStatistics
    } finally {
        lock.writeLock().unlock()
    }


    fun delete(index: Int) = try {
        lock.writeLock().lock()
        dataStore[index] = TransactionStatistics()
    } finally {
        lock.writeLock().unlock()
    }


    fun search(index: Int): TransactionStatistics = try {
        lock.readLock().lock()
        dataStore[index]
    } finally {
        lock.readLock().unlock()
    }

    fun getTransactions(): List<TransactionStatistics> = try {
        lock.readLock().lock()
        dataStore.filter { it.count > 0 }
    } finally {
        lock.readLock().unlock()
    }

}

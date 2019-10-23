package rt.com.n26challenge.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import rt.com.n26challenge.exception.TransactionException
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.repository.TransactionRepository


@Service
class TransactionService(
        @Autowired val statisticRepository: TransactionRepository,
        @Autowired val transactionComputer: TransactionComputer
) {

    fun addTransaction(transaction: Transaction) {
        if (transaction.isValid()) {
            val transactionStatistics = transactionComputer.compute(transaction)
            statisticRepository.add(transactionComputer.computeIndex(transaction.timestamp), transactionStatistics)
        } else
            throw TransactionException("OutdatedTransaction")
    }
}


package rt.com.n26challenge.factories

import rt.com.n26challenge.model.Transaction
import java.time.Instant

class CurrentTransaction : Transaction(
        amount = 5.0,
        timestamp = Instant.now().toEpochMilli()
)

class OldTransaction : Transaction(
        amount = 5.0,
        timestamp = Instant.now().minusSeconds(60).toEpochMilli()
)

class FutureTransaction : Transaction(
        amount = 5.0,
        timestamp = Instant.now().plusSeconds(10).toEpochMilli()
)

enum class TransactionType {
    CURRENT, OLD, FUTURE
}

class TransactionFactory {
    companion object {
        fun create(transactionType: TransactionType): Transaction {
            when (transactionType) {
                TransactionType.CURRENT -> return CurrentTransaction()
                TransactionType.OLD -> return OldTransaction()
                TransactionType.FUTURE -> return FutureTransaction()
            }
        }
    }
}

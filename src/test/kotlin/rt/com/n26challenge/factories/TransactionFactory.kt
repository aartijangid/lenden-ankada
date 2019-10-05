package rt.com.n26challenge.factories

import rt.com.n26challenge.service.Transaction
import java.time.Instant

class CurrentTransaction : Transaction(
        amount = 5.0,
        timestamp = Instant.now().epochSecond
)

class OldTransaction : Transaction(
        amount = 5.0,
        timestamp = Instant.now().minusSeconds(60).epochSecond
)

enum class TransactionType {
    CURRENT, OLD
}

class TransactionFactory {
    companion object {
        fun create(transactionType: TransactionType): Transaction {
            when (transactionType) {
                TransactionType.CURRENT -> return CurrentTransaction()
                TransactionType.OLD -> return OldTransaction()
            }
        }
    }
}

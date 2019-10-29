package rt.com.n26challenge.fixtures

import rt.com.n26challenge.fixtures.TransactionType.CURRENT
import rt.com.n26challenge.fixtures.TransactionType.FUTURE
import rt.com.n26challenge.fixtures.TransactionType.OLD
import rt.com.n26challenge.model.Transaction
import java.time.Instant

val currentTransaction = Transaction(
    amount = 5.0,
    timestamp = Instant.now().toEpochMilli()
)

val oldTransaction = Transaction(
    amount = 5.0,
    timestamp = Instant.now().minusSeconds(60).toEpochMilli()
)

val futureTransaction = Transaction(
    amount = 5.0,
    timestamp = Instant.now().plusSeconds(10).toEpochMilli()
)

enum class TransactionType {
    CURRENT, OLD, FUTURE
}

fun TransactionType.create() = when (this) {
    CURRENT -> currentTransaction
    OLD -> oldTransaction
    FUTURE -> futureTransaction

}
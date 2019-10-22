package rt.com.n26challenge.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rt.com.n26challenge.factories.TransactionFactory
import rt.com.n26challenge.factories.TransactionType

internal class TransactionTest {

    @Test
    fun `validate - given current timestamp in seconds should return true`() {
        val transaction = TransactionFactory.create(TransactionType.CURRENT)
        assertEquals(true, transaction.isValid())
    }

    @Test
    fun `validate - given future timestamp in seconds should return false`() {
        val transaction = TransactionFactory.create(TransactionType.FUTURE)
        assertEquals(false, transaction.isValid())
    }

    @Test
    fun `validate - given timestamp 60 seconds older should return false`() {
        val transaction = TransactionFactory.create(TransactionType.OLD)
        assertEquals(false, transaction.isValid())
    }
}
package rt.com.n26challenge.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rt.com.n26challenge.fixtures.TransactionType.CURRENT
import rt.com.n26challenge.fixtures.TransactionType.FUTURE
import rt.com.n26challenge.fixtures.TransactionType.OLD
import rt.com.n26challenge.fixtures.create

internal class TransactionTest {

    @Test
    fun `validate - given current timestamp in seconds should return true`() {
        val transaction = CURRENT.create()
        assertEquals(true, transaction.isValid())
    }

    @Test
    fun `validate - given future timestamp in seconds should return false`() {
        val transaction = FUTURE.create()
        assertEquals(false, transaction.isValid())
    }

    @Test
    fun `validate - given timestamp 60 seconds older should return false`() {
        val transaction = OLD.create()
        assertEquals(false, transaction.isValid())
    }

}
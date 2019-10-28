package rt.com.n26challenge.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import rt.com.n26challenge.factories.TransactionType
import rt.com.n26challenge.factories.create

internal class TransactionTest {

    @Test
    fun `validate - given current timestamp in seconds should return true`() {
        val transaction = TransactionType.CURRENT.create()
        assertThat(transaction.isValid()).isTrue
    }

    @Test
    fun `validate - given future timestamp in seconds should return false`() {
        val transaction = TransactionType.FUTURE.create()
        assertThat(transaction.isValid()).isFalse
    }

    @Test
    fun `validate - given timestamp 60 seconds older should return false`() {
        val transaction = TransactionType.OLD.create()
        assertThat(transaction.isValid()).isFalse
    }
}
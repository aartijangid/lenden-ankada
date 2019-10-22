package rt.com.n26challenge.model

import java.time.ZoneOffset
import java.time.ZonedDateTime

open class Transaction(var amount: Double, var timestamp: Long) {
    override fun equals(other: Any?): Boolean =
            if (other is Transaction) {
                amount == other.amount &&
                        timestamp == other.timestamp
            } else {
                false
            }

    override fun hashCode(): Int {
        var result = amount.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

    fun isValid(): Boolean {
        println("in validate $timestamp")
        val utcTimeZone = ZonedDateTime.now(ZoneOffset.UTC)
        val currentTimestamp = utcTimeZone.toEpochSecond()
        val timestampInSeconds = timestamp / 1000
        println(currentTimestamp.minus(timestampInSeconds) in 0..59)
        return currentTimestamp.minus(timestampInSeconds) in 0..59
    }
}
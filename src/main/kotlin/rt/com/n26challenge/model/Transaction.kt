package rt.com.n26challenge.model

import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

data class Transaction(val amount: Double, val timestamp: Long) {

    fun isValid(): Boolean {
        val utcTimeZone = ZonedDateTime.now(UTC)
        val currentTimestamp = utcTimeZone.toEpochSecond()
        val timestampInSeconds = timestamp / 1000
        println(currentTimestamp.minus(timestampInSeconds) in 0..59)
        return currentTimestamp.minus(timestampInSeconds) in 0..59
    }
}
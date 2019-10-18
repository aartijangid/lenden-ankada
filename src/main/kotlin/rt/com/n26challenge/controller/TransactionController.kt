package rt.com.n26challenge.controller


import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rt.com.n26challenge.service.Transaction
import rt.com.n26challenge.service.TransactionRequest
import rt.com.n26challenge.service.TransactionService
import java.time.ZoneOffset
import java.time.ZonedDateTime

@RestController
class TransactionController(val transactionService: TransactionService) {

    @PostMapping("/transactions", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addTransaction(@RequestBody transactionRequest: TransactionRequest): ResponseEntity<Any> {
        val utcTimeZone = ZonedDateTime.now(ZoneOffset.UTC)
        val currentTimestamp = utcTimeZone.toEpochSecond()
        val timestampInSeconds = transactionRequest.timestamp / 1000

        return if (currentTimestamp.minus(timestampInSeconds) in 0..59) {
            val transaction = Transaction(timestamp = transactionRequest.timestamp, amount = transactionRequest.amount)
            transactionService.addTransaction(transaction)
            ResponseEntity(RequestStatus.ACCEPTED.requestStatus)
        } else
            ResponseEntity(RequestStatus.REJECTED.requestStatus)
    }
}

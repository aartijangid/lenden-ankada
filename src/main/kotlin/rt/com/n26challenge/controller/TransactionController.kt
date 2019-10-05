package rt.com.n26challenge.controller


import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rt.com.n26challenge.service.TransactionRequest
import java.time.Instant

@RestController
class TransactionController {

    @PostMapping("/transactions", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addTransaction(@RequestBody transactionRequest: TransactionRequest): ResponseEntity<Any> {
        return if (Instant.now().epochSecond.minus(transactionRequest.timestamp) in 0..59)
            ResponseEntity(HttpStatus.CREATED)
        else
            ResponseEntity(HttpStatus.NO_CONTENT)
    }
}


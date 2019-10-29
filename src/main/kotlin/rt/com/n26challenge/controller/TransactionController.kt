package rt.com.n26challenge.controller


import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rt.com.n26challenge.exception.TransactionException
import rt.com.n26challenge.model.Transaction
import rt.com.n26challenge.service.TransactionService
import javax.validation.Valid

@RestController
class TransactionController(val transactionService: TransactionService) {

    @PostMapping("/transactions", consumes = [APPLICATION_JSON_VALUE])
    fun addTransaction(@Valid @RequestBody transactionRequest: TransactionRequest): ResponseEntity<Any> =
        try {
            transactionService.addTransaction(
                Transaction(
                    timestamp = transactionRequest.timestamp,
                    amount = transactionRequest.amount
                )
            )
            ResponseEntity(CREATED)
        } catch (transactionException: TransactionException) {
            ResponseEntity(NO_CONTENT)
        }
}

data class TransactionRequest(
    @JsonProperty(required = true)
    val amount: Double,

    @JsonProperty(required = true)
    val timestamp: Long
)
package rt.com.n26challenge.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import rt.com.n26challenge.service.Transaction
import rt.com.n26challenge.service.TransactionRequest
import rt.com.n26challenge.service.TransactionService
import java.time.Instant

@WebMvcTest(controllers = [TransactionController::class])
class TransactionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var transactionService: TransactionService

    private val now = Instant.now().toEpochMilli()

    @Test
    fun `given transaction is happening now should return 201`() {
        val transactionRequest = TransactionRequest(
                amount = 5.0,
                timestamp = now
        )

        val transaction = Transaction(
                amount = transactionRequest.amount,
                timestamp = transactionRequest.timestamp
        )

        Mockito.doNothing().`when`(transactionService).addTransaction(transaction)

        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated)

        verify(transactionService).addTransaction(transaction)
    }

    @Test
    fun `given transaction is 60 seconds older should return 201`() {
        val transactionRequest = TransactionRequest(
                amount = 5.0,
                timestamp = now - 59000
        )

        val transaction = Transaction(
                amount = transactionRequest.amount,
                timestamp = transactionRequest.timestamp
        )

        Mockito.doNothing().`when`(transactionService).addTransaction(transaction)

        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated)

        verify(transactionService).addTransaction(transaction)
    }

    @Test
    fun `given transaction is exactly older than 60 seconds should return 204`() {
        // given
        val transactionRequest = TransactionRequest(
                amount = 5.0,
                timestamp = now - 60001
        )

        // then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transactionRequest)))
                .andExpect(status().isNoContent)

        verifyZeroInteractions(transactionService)
    }

    @Test
    fun `given future transaction should return 204`() {
        // given
        val transaction = TransactionRequest(
                amount = 5.0,
                timestamp = now + 1100
        )

        // then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isNoContent)

        verifyZeroInteractions(transactionService)
    }
}

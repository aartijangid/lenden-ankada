package rt.com.n26challenge.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import rt.com.n26challenge.service.TransactionRequest
import java.time.Instant

@WebMvcTest
class TransactionControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val now = Instant.now()

    @Test
    fun `given transaction is happening now should return 201`() {
        val transaction = TransactionRequest(
                amount = 5.0,
                timestamp = now.epochSecond
        )
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isCreated)
    }

    @Test
    fun `given transaction is 60 seconds older should return 201`() {
        val transaction = TransactionRequest(
                amount = 5.0,
                timestamp = now.epochSecond - 59
        )
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isCreated)
    }

    @Test
    fun `given transaction is exactly older than 60 seconds should return 204`() {
        // given
        val transaction = TransactionRequest(
                amount = 5.0,
                timestamp = now.epochSecond - 61
        )
        // then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isNoContent)
    }

    @Test
    fun `given future transaction should return 204`() {
        // given
        val transaction = TransactionRequest(
                amount = 5.0,
                timestamp = now.epochSecond + 1
        )

        // then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isNoContent)
    }
}

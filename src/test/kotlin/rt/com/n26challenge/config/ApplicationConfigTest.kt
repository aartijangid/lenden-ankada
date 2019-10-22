package rt.com.n26challenge.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import rt.com.n26challenge.controller.TransactionRequest

@SpringBootTest
class ApplicationConfigTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should fail for missing data`() {
        val json = """ { } """

        assertThatThrownBy { objectMapper.readValue<TransactionRequest>(json) }
                .isInstanceOf(MismatchedInputException::class.java)
                .hasMessageContaining("Please provide an amount")
    }
}
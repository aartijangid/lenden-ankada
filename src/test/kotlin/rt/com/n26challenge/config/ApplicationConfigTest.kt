package rt.com.n26challenge.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import rt.com.n26challenge.controller.TransactionRequest

@SpringBootTest
@ExtendWith(SpringExtension::class)
class ApplicationConfigTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should fail for missing data`() {
        val json = """ { } """

        assertThatThrownBy { objectMapper.readValue<TransactionRequest>(json) }
            .isInstanceOf(MismatchedInputException::class.java)
            .hasMessageContaining("Missing required creator property 'amount'")
    }
}
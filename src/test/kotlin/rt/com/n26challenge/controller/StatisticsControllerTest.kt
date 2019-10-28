package rt.com.n26challenge.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import rt.com.n26challenge.service.StatisticsService

@WebMvcTest(controllers = [StatisticsController::class])
internal class StatisticsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var statisticsService: StatisticsService

    @Test
    fun `GET statistics should return status 200 OK with statistics for last 60 seconds`() {
        // given
        val statisticsResponse = StatisticsResponse()
        given(statisticsService.getStatistics()).willReturn(statisticsResponse)

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/statistics")
                .accept(APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(statisticsResponse)))

        verify(statisticsService).getStatistics()
    }
}

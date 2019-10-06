package rt.com.n26challenge.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import rt.com.n26challenge.service.StatisticsService

@WebMvcTest(controllers = [StatisticsController::class])
internal class StatisticsControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var statisticsService: StatisticsService

    @Test
    fun `GET statistics should return status 200 OK with statistics for last 60 seconds`() {
        // given
        val statisticsResponse = StatisticsResponse()
        given(statisticsService.get()).willReturn(statisticsResponse)

        // when
        val result = mockMvc.perform(MockMvcRequestBuilders
                .get("/statistics")
                .accept(MediaType.APPLICATION_JSON))

        // then
        val objectMapper = ObjectMapper()
        result.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().json(objectMapper.writeValueAsString(statisticsResponse)))

        verify(statisticsService).get()
    }
}

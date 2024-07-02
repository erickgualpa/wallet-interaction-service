package org.egualpam.contexts.payment.walletinteractionservice.health

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.AbstractIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ApplicationHealthTest : AbstractIntegrationTest() {

    @Test
    fun `application status should be UP`() {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                      "status": "UP"
                    }
                    """
                )
            )
    }
}
package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [WalletInteractionServiceApplication::class],
)
abstract class AbstractIntegrationTest {
  @Autowired
  protected lateinit var mockMvc: MockMvc
}

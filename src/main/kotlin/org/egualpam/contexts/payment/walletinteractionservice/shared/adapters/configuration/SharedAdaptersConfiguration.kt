package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(RabbitMqProperties::class)
class SharedAdaptersConfiguration {
  @Bean
  fun domainEventIdSupplier(): DomainEventIdSupplier = object : DomainEventIdSupplier {
    override fun get() = DomainEventId.generate()
  }
}

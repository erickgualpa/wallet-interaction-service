package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.eventbus.rabbitmq.RabbitMQEventBus
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SharedAdaptersConfiguration {
  @Bean
  fun domainEventIdSupplier(): DomainEventIdSupplier = object : DomainEventIdSupplier {
    override fun get() = DomainEventId.generate()
  }

  @Bean
  fun eventBus(): EventBus = RabbitMQEventBus()
}

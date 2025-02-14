package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration

import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEvent
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventId
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.DomainEventIdSupplier
import org.egualpam.contexts.payment.walletinteractionservice.shared.application.ports.out.EventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SharedAdaptersConfiguration {
  @Bean
  fun domainEventIdSupplier(): DomainEventIdSupplier = object : DomainEventIdSupplier {
    override fun get() = DomainEventId.generate()
  }

  @Bean
  fun fakeEventBus(): EventBus {
    return object : EventBus {
      private val logger: Logger = LoggerFactory.getLogger(this::class.java)
      override fun publish(domainEvents: Set<DomainEvent>) {
        domainEvents.forEach {
          logger.info("Fake publishing of event [${it.javaClass.simpleName}] with id [${it.id().value}]")
        }
      }
    }
  }
}
